package com.caijiale.myrpc.core.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.caijiale.myrpc.core.model.RpcRequest;
import com.caijiale.myrpc.core.model.RpcResponse;
import com.caijiale.myrpc.core.serializer.JdkSerializer;
import com.caijiale.myrpc.core.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理（JDK 动态代理）
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取方法的返回类型，根据返回类型生成默认的调用对象
        Class<?> returnType = method.getReturnType();
        log.info("Mock invoke method: {}", method.getName());
        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> returnType) {
        //基本类型
        if (returnType.isPrimitive()) {
            if (returnType == int.class) {
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == byte.class) {
                return (byte) 0;
            } else if (returnType == float.class) {
                return 0.0f;
            } else if (returnType == double.class) {
                return 0.0d;
            } else if (returnType == char.class) {
                return '\u0000';
            } else if (returnType == boolean.class) {
                return false;
            }
        }
        return null;

    }
}
