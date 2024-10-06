package com.caijiale.myrpc.core.proxy;

import cn.hutool.core.collection.CollUtil;
import com.caijiale.myrpc.core.RpcApplication;
import com.caijiale.myrpc.core.config.RpcConfig;
import com.caijiale.myrpc.core.constant.RpcConstant;
import com.caijiale.myrpc.core.fault.retry.RetryStrategy;
import com.caijiale.myrpc.core.fault.retry.RetryStrategyFactory;
import com.caijiale.myrpc.core.fault.tolerant.TolerantStrategyFactory;
import com.caijiale.myrpc.core.loadbalancer.LoadBalancer;
import com.caijiale.myrpc.core.loadbalancer.LoadBalancerFactory;
import com.caijiale.myrpc.core.model.RpcRequest;
import com.caijiale.myrpc.core.model.RpcResponse;
import com.caijiale.myrpc.core.model.ServiceMetaInfo;
import com.caijiale.myrpc.core.registry.Registry;
import com.caijiale.myrpc.core.registry.RegistryFactory;
import com.caijiale.myrpc.core.serializer.Serializer;
import com.caijiale.myrpc.core.serializer.SerializerFactory;
import com.caijiale.myrpc.core.server.tcp.VertxTcpClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * 服务代理（JDK 动态代理）
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            log.error("暂无服务地址：key：{}", serviceMetaInfo.getServiceKey());
            throw new RuntimeException("暂无服务地址");
        }
        //负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        HashMap<String, Object> requesParams = new HashMap<>();
        requesParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requesParams, serviceMetaInfoList);
        // 使用重试机制
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    // 发送 TCP 请求
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
        } catch (Exception e) {
            //容错处理
            rpcResponse = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy()).doTolerant(null, e);
        }
        log.info("调用成功 " + rpcResponse.getData());
        return rpcResponse.getData();
    }
}
