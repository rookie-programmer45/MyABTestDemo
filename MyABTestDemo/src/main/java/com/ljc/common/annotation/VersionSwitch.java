package com.ljc.common.annotation;

import java.lang.annotation.*;

/**
 * 用于标注当前方法所使用的版本
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VersionSwitch {
    String value() default "";
}
