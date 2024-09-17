package com.caijiale.myrpc.consumer;

import com.caijiale.myrpc.common.model.User;
import com.caijiale.myrpc.common.service.UserService;
import com.caijiale.myrpc.core.config.RpcConfig;
import com.caijiale.myrpc.core.proxy.ServiceProxyFactory;
import com.caijiale.myrpc.core.utils.ConfigUtils;

/**
 * 简易服务消费者示例
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("cjl");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
