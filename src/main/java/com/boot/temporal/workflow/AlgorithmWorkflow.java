package com.boot.temporal.workflow;

import com.boot.config.TemporalConfig;
import com.boot.temporal.po.WorkerStreamReq;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface AlgorithmWorkflow {
    @WorkflowMethod
    void run(WorkerStreamReq req, TemporalConfig config);
}
