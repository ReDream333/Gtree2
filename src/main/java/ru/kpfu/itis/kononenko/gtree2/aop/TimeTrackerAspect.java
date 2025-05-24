package ru.kpfu.itis.kononenko.gtree2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeTrackerAspect {

    @Around("@annotation(ru.kpfu.itis.kononenko.gtree2.aop.Timed)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            log.debug("{} executed in {} ms", pjp.getSignature().toShortString(), ms);
        }
    }
}
