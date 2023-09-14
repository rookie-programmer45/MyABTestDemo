package com.ljc.common.config;

import com.ljc.common.annotation.RoutingInjected;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class RoutingProcessor implements BeanPostProcessor, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object beInjectedBean = getTargetBean(bean);
        Class<?> targetType = beInjectedBean.getClass();
        Field[] fields = targetType.getDeclaredFields();

        for (Field field : fields) {
            if (!field.isAnnotationPresent(RoutingInjected.class)) {
                continue;
            }
            try {
                routingInject(field, beInjectedBean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    /**
     * 若要注入的bean是代理对象，则需要从代理对象中获取原target对象。非代理对象则直接返回原对象即可
     * @param bean
     * @return
     */
    private Object getTargetBean(Object bean) {
        if (bean instanceof Advised) {
            return AopProxyUtils.getSingletonTarget(bean);
        }
        return bean;
    }

    private void routingInject(Field field, Object bean) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        Map<String, ?> injectedCandidate = context.getBeansOfType(fieldType);
        field.setAccessible(true);

        if (injectedCandidate.size() == 1) {
            field.set(bean, injectedCandidate.values().iterator().next());
        } else if (injectedCandidate.size() == 2) {
            field.set(bean, RoutingProxyFactory.createProxy(fieldType, injectedCandidate));
        } else {
            throw new IllegalStateException("more than 2 routing beans, type: " + fieldType);
        }
    }
}
