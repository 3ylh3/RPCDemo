package com.xiaobai.rpcdemo.core.consumer.annotation;

import com.xiaobai.rpcdemo.core.enums.LoadBalanceEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {
    String providerName() default "";
    String group() default "";
    LoadBalanceEnum loadBlance() default LoadBalanceEnum.ROUND;
}
