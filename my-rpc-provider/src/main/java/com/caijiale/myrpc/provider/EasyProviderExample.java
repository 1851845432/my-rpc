package com.caijiale.myrpc.provider;


import com.caijiale.myrpc.common.service.UserService;
import com.caijiale.myrpc.core.RpcApplication;
import com.caijiale.myrpc.core.registry.LocalRegistry;
import com.caijiale.myrpc.core.server.HttpServer;
import com.caijiale.myrpc.core.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();
        
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
