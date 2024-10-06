package com.caijiale.myrpcspringbootprovider;

import com.caijiale.myrpc.springbootstarter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc
public class MyRpcSpringBootProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyRpcSpringBootProviderApplication.class, args);
    }

}
