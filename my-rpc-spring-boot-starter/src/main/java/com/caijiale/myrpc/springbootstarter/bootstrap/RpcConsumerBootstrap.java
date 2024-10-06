package com.caijiale.myrpc.springbootstarter.bootstrap;

import com.caijiale.myrpc.core.proxy.ServiceProxyFactory;
import com.caijiale.myrpc.springbootstarter.annotation.RpcReference;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Rpc 服务消费者启动
 */
@Slf4j
public class RpcConsumerBootstrap implements BeanPostProcessor {

    /**
     * 在 Bean 初始化后执行，用于注入服务
     * 该方法主要负责处理带有 RpcReference 注解的字段，通过反射机制为这些字段创建并注入代理对象
     *
     * @param bean     需要处理的 Bean 对象
     * @param beanName Bean 的名称
     * @return 处理后的 Bean 对象
     * @throws BeansException 当处理过程中出现错误时抛出的异常
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取 Bean 的类信息
        Class<?> beanClass = bean.getClass();
        // 遍历对象的所有属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 检查属性是否带有 RpcReference 注解
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                // 为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                // 如果未指定接口类，则使用字段的类型作为接口类
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                // 设置字段可访问，以便注入代理对象
                field.setAccessible(true);
                // 创建接口类的代理对象
                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    // 将代理对象注入到 Bean 的字段中
                    field.set(bean, proxyObject);
                    // 恢复字段的访问权限设置
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    // 如果注入失败，则抛出运行时异常
                    throw new RuntimeException("为字段注入代理对象失败", e);
                }
            }
        }
        // 调用父类方法处理其他初始化后操作
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

}
