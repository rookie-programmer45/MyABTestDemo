package com.ljc.common.config;

import com.ljc.common.annotation.RoutingVersion;
import com.ljc.common.annotation.VersionSwitch;
import com.ljc.common.enums.RoutingVersionEnum;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

public class RoutingProxyFactory {

    public static Object createProxy(Class<?> beProxiedTargetType, Map<String, ?> candidate) {
        ProxyFactory proxyFactory = new ProxyFactory();
        if (!beProxiedTargetType.isInterface()) {
            throw new IllegalArgumentException("Routing Bean must be interface!");
        }
        proxyFactory.setInterfaces(beProxiedTargetType);
        proxyFactory.addAdvice(new RoutingMethodInterceptor(candidate));
        return proxyFactory.getProxy();
    }

    private static class RoutingMethodInterceptor implements MethodInterceptor {

        private Object beanVA;
        private Object beanVB;
        private Random random = new Random();

        public RoutingMethodInterceptor(Map<String, ?> candidate) {
            for (Map.Entry<String, ?> entry : candidate.entrySet()) {
                RoutingVersionEnum version = entry.getValue().getClass().getAnnotation(RoutingVersion.class).version();
                if (RoutingVersionEnum.A.equals(version) && beanVA == null) {
                    beanVA = entry.getValue();
                } else {
                    beanVB = entry.getValue();
                }
            }
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();
            VersionSwitch versionSwitch = method.getAnnotation(VersionSwitch.class);

            return invocation.getMethod().invoke(getTargetBean(versionSwitch), invocation.getArguments());
        }

        private Object getTargetBean(VersionSwitch versionSwitch) {
            // 未指定AB测试版本时，随机调用某个版本的Bean
            if (versionSwitch == null || versionSwitch.value().isBlank()) {
                return random();
            }

            if (RoutingVersionEnum.A.getValue().equals(versionSwitch.value())) {
                return beanVA;
            } else if (RoutingVersionEnum.B.getValue().equals(versionSwitch.value())) {
                return beanVB;
            } else {
                throw new IllegalArgumentException("unknow routing version: " + versionSwitch.value());
            }
        }

        /**
         * 随机获取一个bean
         * @return
         */
        private Object random() {
            if (random.nextInt(2) == 0) {
                return beanVA;
            }
            return beanVB;
        }
    }
}
