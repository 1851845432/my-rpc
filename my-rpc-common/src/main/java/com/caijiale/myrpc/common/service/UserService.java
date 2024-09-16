package com.caijiale.myrpc.common.service;

import com.caijiale.myrpc.common.model.User;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);
    /**
     * 新方法 获取数字测试
     */
    default int getNum(){
        return 1;
    }
}
