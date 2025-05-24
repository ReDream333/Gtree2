// src/main/java/ru/.../aop/RateLimitAspect.java
package ru.kpfu.itis.kononenko.gtree2.aop;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> globalBuckets = new ConcurrentHashMap<>();

    final
    HttpServletRequest request;

    public RateLimitAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Around("@annotation(limit)")
    public Object around(ProceedingJoinPoint jp, RateLimit limit) throws Throwable {

        Bucket bucket = getBucket(jp, limit);

        if (bucket.tryConsume(1)) {
            return jp.proceed();
        }
        log.warn("Rate-limit exceeded for {}", jp.getSignature());
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                "Rate limit exceeded");
    }

    private Bucket getBucket(ProceedingJoinPoint jp, RateLimit limit) {

        Refill refill = Refill.greedy(limit.permits(),
                                      Duration.of(limit.duration(), limit.unit().toChronoUnit()));
        Bandwidth bw = Bandwidth.classic(limit.permits(), refill);

        String key;
        Map<String, Bucket> mapToUse;

        if (limit.global()) {
            key = jp.getSignature().toLongString();
            mapToUse = globalBuckets;
        } else {
            String ip = request != null ? request.getRemoteAddr() : "unknown";
            key = jp.getSignature().toLongString() + "|" + ip;
            mapToUse = buckets;
        }
        return mapToUse.computeIfAbsent(key, k -> Bucket.builder().addLimit(bw).build());
    }
}
