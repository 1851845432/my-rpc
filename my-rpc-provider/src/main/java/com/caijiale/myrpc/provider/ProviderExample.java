package com.caijiale.myrpc.provider;


import com.caijiale.myrpc.common.service.UserService;
import com.caijiale.myrpc.core.RpcApplication;
import com.caijiale.myrpc.core.bootstrap.ProviderBootstrap;
import com.caijiale.myrpc.core.config.RegistryConfig;
import com.caijiale.myrpc.core.config.RpcConfig;
import com.caijiale.myrpc.core.model.ServiceMetaInfo;
import com.caijiale.myrpc.core.model.ServiceRegisterInfo;
import com.caijiale.myrpc.core.registry.LocalRegistry;
import com.caijiale.myrpc.core.registry.Registry;
import com.caijiale.myrpc.core.registry.RegistryFactory;
import com.caijiale.myrpc.core.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * 服务提供者示例
 */
public class ProviderExample {

    public static void main(String[] args) {
        // 服务注册信息
        UserService userService = new UserServiceImpl();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), userService.getClass());

        // 服务提供者初始化
        ProviderBootstrap.init(List.of(serviceRegisterInfo));
    }
}
