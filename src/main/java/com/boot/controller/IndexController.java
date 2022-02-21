package com.boot.controller;

import com.boot.temporal.AlgorithmWorker;
import com.boot.temporal.po.WorkerStreamReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 功能描述：
 *
 * @program: temporal-springboot-template
 * @author: 代号007
 * @create: 2022-02-21 15:43
 **/
@Slf4j
@RestController
@Api(value = "首页", tags = "首页")
@RequiredArgsConstructor
public class IndexController {
    private final AlgorithmWorker algorithmWorker;

    @ApiOperation("发送测试")
    @GetMapping("/send")
    public String send() {
        WorkerStreamReq workerStreamReq = new WorkerStreamReq();
        workerStreamReq.setSequence(UUID.randomUUID().toString());
        workerStreamReq.setMemo("0");
        workerStreamReq.setType(1);
        algorithmWorker.send(workerStreamReq);
        return "发送成功";
    }
}
