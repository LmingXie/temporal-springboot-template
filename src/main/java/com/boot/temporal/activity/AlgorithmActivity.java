package com.boot.temporal.activity;

import com.boot.temporal.po.WorkerStreamReq;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AlgorithmActivity {

    @ActivityMethod
    WorkerStreamReq algorithm1(WorkerStreamReq req);

    @ActivityMethod
    void print(WorkerStreamReq req);

    @ActivityMethod
    boolean db(WorkerStreamReq req);

    @ActivityMethod
    String redis(WorkerStreamReq req);

    @ActivityMethod
    void clearSerializationCache(String sequence, String memo);
}
