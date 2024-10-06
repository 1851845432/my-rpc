package com.caijiale.myrpcspringbootconsumer;

import com.caijiale.myrpc.springbootstarter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc(needServer = false)
public class MyRpcSpringBootConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyRpcSpringBootConsumerApplication.class, args);
    }

}
