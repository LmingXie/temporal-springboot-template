package com.boot.temporal.workflow;

import com.boot.common.Shared;
import com.boot.config.TemporalConfig;
import com.boot.temporal.activity.AlgorithmActivity;
import com.boot.temporal.po.WorkerStreamReq;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.DurationStyle;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AlgorithmWorkflowImpl implements AlgorithmWorkflow {

    // 传输方法是工作流的入口点。
    // 活动方法的执行可以在此处或从其他活动方法中进行编排
    @Override
    public void run(WorkerStreamReq req, TemporalConfig config) {
        log.info("开始执行编排逻辑。");
        // RetryOptions 指定如何在活动失败时自动处理重试。
        RetryOptions retryoptions = RetryOptions.newBuilder()
                .setMaximumInterval(DurationStyle.detectAndParse(config.getMaximumInterval()))
                .setInitialInterval(DurationStyle.detectAndParse(config.getInitialInterval()))
                .setBackoffCoefficient(config.getBackoffCoefficient())
//                .setMaximumAttempts(config.getMaximumAttempts())
                .build();
        ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
                // 超时选项指定如果进程花费太长时间，何时自动超时活动。
                .setStartToCloseTimeout(DurationStyle.detectAndParse(config.getStartToCloseTimeout()))
                .setHeartbeatTimeout(DurationStyle.detectAndParse(config.getHeartbeatTimeout()))
                // （可选）提供自定义的 RetryOptions。
                // 默认情况下临时重试失败，这只是一个示例。
                .setRetryOptions(retryoptions)
                // 一个工作流愿意等待活动完成的总时间(限制活动的总执行时间)，包括重试,默认无限制
//                .setScheduleToCloseTimeout(DurationStyle.detectAndParse(config.getScheduleToCloseTimeout()))
//                .setScheduleToStartTimeout(DurationStyle.detectAndParse(config.getScheduleToStartTimeout()))
                .build();

        AlgorithmActivity algorithmActivity = Workflow.newActivityStub(AlgorithmActivity.class, defaultActivityOptions);
        String sequence = req.getSequence(), memo = req.getMemo();

        log.info("开始执行编排逻辑。sequence：{} memo：{}", sequence, memo);


        List<Promise<Void>> promises = new ArrayList<>();
//        int version = Workflow.getVersion("checksumAdded", Workflow.DEFAULT_VERSION, 1);
//        if (version == Workflow.DEFAULT_VERSION) {
            algorithmActivity.algorithm1(req);
            algorithmActivity.print(req);
            promises.add(Async.procedure(() -> algorithmActivity.redis(req)));
            promises.add(Async.procedure(() -> algorithmActivity.db(req)));
            log.info("=============旧代码");
//        } else {
//            System.out.println("===========新版本的代码=========");
//            algorithmActivity.redis(req);
//            algorithmActivity.db(req);
//        }
        // wait
        Promise.allOf(promises).get();

        algorithmActivity.clearSerializationCache(sequence, memo);
        log.info("<<<<<执行编排的所有业务逻辑完成>>>>>");
    }
}
// @@@SNIPEND
