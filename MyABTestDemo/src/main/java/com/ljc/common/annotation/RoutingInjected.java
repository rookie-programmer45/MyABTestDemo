package com.ljc.common.annotation;

import java.lang.annotation.*;

/**
 * 用于标注该属性需要被注入AB测试的代理类
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoutingInjected {
}
