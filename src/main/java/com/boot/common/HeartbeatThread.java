package com.boot.common;

import io.temporal.activity.ActivityExecutionContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述：
 *
 * @author: 代号007
 * @create: 2021-12-28 09:39
 **/
@Slf4j
public class HeartbeatThread implements Runnable {
    private Integer heartbeatInterval;
    private ActivityExecutionContext context;
    private Object value;
    private volatile boolean status = true;

    public HeartbeatThread(Integer heartbeatInterval, ActivityExecutionContext context, Object value) {
        this.heartbeatInterval = heartbeatInterval;
        this.context = context;
        this.value = value;
    }

    @Override
    public void run() {
        log.info("启动【{}】心跳包发送任务", value);
        while (status) {
            context.heartbeat(1);
            log.info("发送【{}】心跳包成功。", value);
            try {
                Thread.sleep(heartbeatInterval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void stop() {
        status = false;
    }
}
