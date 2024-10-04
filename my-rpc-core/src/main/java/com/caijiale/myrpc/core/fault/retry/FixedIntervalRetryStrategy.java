package com.caijiale.myrpc.core.fault.retry;

import com.caijiale.myrpc.core.model.RpcResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 固定时间间隔 - 重试策略
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {

    /**
     * 重试方法
     * <p>
     * 此方法使用Retryer框架对给定的Callable任务进行重试，主要用于处理网络通信等可能出现暂时性故障的操作
     * 它定义了在遇到异常时重试的策略，包括重试的等待时间、尝试次数，并且在每次重试前记录重试次数
     *
     * @param callable 一个返回RpcResponse的Callable对象，表示需要重试的任务
     * @return RpcResponse 任务执行成功的返回结果
     * @throws RetryException     当重试过程中发生无法继续的异常时抛出
     * @throws ExecutionException 当任务执行发生异常时抛出
     */
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws RetryException, ExecutionException {
        // 构建重试器，使用泛型RpcResponse指定重试返回类型
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                // 设置重试条件为Exception类型，即遇到Exception时触发重试
                .retryIfExceptionOfType(Exception.class)
                // 设置等待策略，每次重试间隔3秒
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                // 设置停止策略，尝试3次后停止重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                // 设置重试监听器，在每次重试前输出当前重试次数
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数 {}", attempt.getAttemptNumber());
                    }
                })
                // 构建重试器实例
                .build();
        // 使用重试器执行任务并返回结果
        return retryer.call(callable);
    }

}
