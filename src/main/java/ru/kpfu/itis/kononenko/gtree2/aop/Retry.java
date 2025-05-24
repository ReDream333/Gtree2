package ru.kpfu.itis.kononenko.gtree2.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int attempts() default 3;
    long backoff() default 200;
    Class<? extends Throwable>[] on() default { Exception.class };
}