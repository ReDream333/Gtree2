// src/main/java/ru/…/aop/RetryAspect.java
package ru.kpfu.itis.kononenko.gtree2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object around(ProceedingJoinPoint pjp, Retry retry) throws Throwable {
        int attempts = retry.attempts();
        long delay    = retry.backoff();
        Class<? extends Throwable>[] catchable = retry.on();

        int tryNo = 1;
        while (true) {
            try {
                return pjp.proceed();
            } catch (Throwable ex) {
                boolean shouldRetry = java.util.Arrays.stream(catchable)
                        .anyMatch(clazz -> clazz.isAssignableFrom(ex.getClass()));
                if (!shouldRetry || tryNo >= attempts) throw ex;

                log.warn("{} failed ({} of {}), retrying in {} ms: {}",
                        pjp.getSignature().toShortString(), tryNo, attempts, delay, ex.getMessage());
                Thread.sleep(delay);
                delay *= 2;
                tryNo++;
            }
        }
    }
}
