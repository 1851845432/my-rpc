package com.caijiale.myrpc.provider;


import com.caijiale.myrpc.common.service.UserService;
import com.caijiale.myrpc.core.RpcApplication;
import com.caijiale.myrpc.core.config.RegistryConfig;
import com.caijiale.myrpc.core.config.RpcConfig;
import com.caijiale.myrpc.core.model.ServiceMetaInfo;
import com.caijiale.myrpc.core.registry.LocalRegistry;
import com.caijiale.myrpc.core.registry.Registry;
import com.caijiale.myrpc.core.registry.RegistryFactory;
import com.caijiale.myrpc.core.server.HttpServer;
import com.caijiale.myrpc.core.server.VertxHttpServer;

/**
 * 服务提供者示例
 */
public class ProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
