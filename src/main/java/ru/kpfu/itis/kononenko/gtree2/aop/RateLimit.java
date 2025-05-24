// src/main/java/ru/.../aop/RateLimit.java
package ru.kpfu.itis.kononenko.gtree2.aop;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    long permits();
    long duration();
    TimeUnit unit() default TimeUnit.MINUTES;
    boolean global() default false;
}
