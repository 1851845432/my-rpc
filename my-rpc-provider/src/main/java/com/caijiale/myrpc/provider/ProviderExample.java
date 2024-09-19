package com.caijiale.myrpc.provider;


import com.caijiale.myrpc.common.service.UserService;
import com.caijiale.myrpc.core.RpcApplication;
import com.caijiale.myrpc.core.config.RegistryConfig;
import com.caijiale.myrpc.core.config.RpcConfig;
import com.caijiale.myrpc.core.model.ServiceMetaInfo;
import com.caijiale.myrpc.core.registry.LocalRegistry;
import com.caijiale.myrpc.core.registry.Registry;
import com.caijiale.myrpc.core.registry.RegistryFactory;
import com.caijiale.myrpc.core.server.tcp.VertxTcpServer;

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

        // 启动 TCP 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(8080);
    }
}
