package com.boot.temporal.activity;

import com.boot.common.BeanContext;
import com.boot.common.HeartbeatThread;
import com.boot.common.Shared;
import com.boot.temporal.manager.Algorithm1Manager;
import com.boot.temporal.manager.BaseManager;
import com.boot.temporal.po.WorkerStreamReq;
import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.activity.ActivityInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgorithmActivityImpl implements AlgorithmActivity {

    private Algorithm1Manager algorithm1Manager;
    private BaseManager baseManager;

    public AlgorithmActivityImpl() {
        log.info("加载初始Bean实例信息");
        baseManager = BeanContext.getBean(BaseManager.class);
        algorithm1Manager = BeanContext.getBean(Algorithm1Manager.class);
    }

    @Override
    public WorkerStreamReq algorithm1(WorkerStreamReq req) {
        ActivityExecutionContext ctx = Activity.getExecutionContext();
        ActivityInfo info = ctx.getInfo();
        String workflowId = info.getWorkflowId();

        HeartbeatThread heartbeatThread = baseManager.startHeartbeatThread(Shared.ALGORITHM1, ctx);

        log.info("{} ------------>>>>> 开始执行【algorithm1】。", workflowId);

        try {
            algorithm1Manager.exec(req);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            heartbeatThread.stop();
        }
        return baseManager.serializationSave(req);
    }

    @Override
    public void print(WorkerStreamReq req) {
        ActivityExecutionContext ctx = Activity.getExecutionContext();
        ActivityInfo info = ctx.getInfo();
        String workflowId = info.getWorkflowId();

        req = baseManager.deserializeGet(req);
        log.info("{} ------------>>>>> 在此处你可以进行【打印】操作。", workflowId);
    }

    @Override
    public boolean db(WorkerStreamReq req) {
        ActivityExecutionContext ctx = Activity.getExecutionContext();
        ActivityInfo info = ctx.getInfo();
        String workflowId = info.getWorkflowId();
        req = baseManager.deserializeGet(req);
        log.info("{} ------------>>>>> 在此处你可以进行【数据持久化】操作。", workflowId);
        return true;
    }

    @Override
    public String redis(WorkerStreamReq req) {
        ActivityExecutionContext ctx = Activity.getExecutionContext();
        ActivityInfo info = ctx.getInfo();
        String workflowId = info.getWorkflowId();
        req = baseManager.deserializeGet(req);
        log.info("{} ------------>>>>> 在此处你可以进行【Redis】操作。", workflowId);
        return "这是Redis数据";
    }

    @Override
    public void clearSerializationCache(String sequence, String memo) {
        baseManager.clearSerializationCache(sequence, memo);
    }
}
