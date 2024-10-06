package com.caijiale.myrpcspringbootprovider;

import com.caijiale.myrpc.common.model.User;
import com.caijiale.myrpc.common.service.UserService;
import com.caijiale.myrpc.springbootstarter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
