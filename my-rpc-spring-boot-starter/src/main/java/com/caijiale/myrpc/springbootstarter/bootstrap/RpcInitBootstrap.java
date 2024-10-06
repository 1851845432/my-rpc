package com.caijiale.myrpc.springbootstarter.bootstrap;


import com.caijiale.myrpc.core.RpcApplication;
import com.caijiale.myrpc.core.config.RpcConfig;
import com.caijiale.myrpc.core.server.tcp.VertxTcpServer;
import com.caijiale.myrpc.springbootstarter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc 框架启动
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    /**
     * Spring 初始化时执行，初始化 RPC 框架
     *
     * @param importingClassMetadata 引入类的元数据，包含注解信息
     * @param registry Bean 定义注册表，用于注册 Bean
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值，用于判断是否需要启动服务器
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");

        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();

        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 根据注解属性值决定是否启动服务器
        if (needServer) {
            // 创建并启动 TCP 服务器
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            // 日志记录，不启动服务器
            log.info("不启动 server");
        }
    }
}
