package com.caijiale.myrpc;


import com.caijiale.myrpc.common.service.UserService;
import com.caijiale.myrpc.registry.LocalRegistry;
import com.caijiale.myrpc.server.HttpServer;
import com.caijiale.myrpc.server.VertxHttpServer;
import io.vertx.core.eventbus.impl.clustered.Serializer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {


    public static void main(String[] args) {
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
