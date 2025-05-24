package ru.kpfu.itis.kononenko.gtree2.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    public static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Pointcut("@annotation(Loggable)")
    public void logExecution(){

    }

    @Around("logExecution()")
    public Object log(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getName();
        LOG.info("Start executing {}.{}", className, methodName);
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        LOG.info("Finish executing {}.{}", className, methodName);
        return result;
    }
}