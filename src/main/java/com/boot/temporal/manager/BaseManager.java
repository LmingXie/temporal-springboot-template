package com.boot.temporal.manager;

import com.alibaba.fastjson.JSON;
import com.boot.common.HeartbeatThread;
import com.boot.common.ObjectSerializable;
import com.boot.common.Shared;
import com.boot.temporal.po.WorkerStreamReq;
import io.temporal.activity.ActivityExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：
 *
 * @program: money-transfer-project-template-java
 * @author: 代号007
 * @create: 2022-02-21 15:14
 **/
@Slf4j
@Component
public class BaseManager {
    @Value("${configuration.temporal.heartbeatInterval:10}")
    private Integer heartbeatInterval;
    @Value("${configuration.temporal.maxConcurrentActivityExecutionSize:200}")
    private Integer maxConcurrentActivityExecutionSize;

    private static ExecutorService executorService;

    @PostConstruct
    public void init() {
        int corePoolSize = maxConcurrentActivityExecutionSize / 2;
        executorService = new ThreadPoolExecutor(corePoolSize, maxConcurrentActivityExecutionSize, 3, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    }

    private String getPath() {
        return "workflow/cache/";
    }
    public WorkerStreamReq serializationSave(WorkerStreamReq req) {
        String key = req.getSequence() + Shared.DELIMITER + req.getMemo();
//        gzipRedisTemplate.opsForHash().put(Shared.SERIALIZATION_PARAM_HASH, key, json);

        try {
            log.debug("开始将对象序列化缓存到本地：{}", key);
            String path = getPath();
            byte[] cache = JSON.toJSONBytes(req);
            ObjectSerializable<byte[]> os = new ObjectSerializable<>();
            os.serialization(path + key, cache);
            log.debug("将对象序列化缓存到本地成功：{}", key);
        } catch (Exception e) {
            log.error("序列化存储异常：{}", key, e);
            throw new RuntimeException( "设置序列化缓存异常:" + key);
        }

        WorkerStreamReq newReq = new WorkerStreamReq();
        newReq.setSequence(req.getSequence());
        newReq.setMemo(req.getMemo());
        return newReq;
    }

    public WorkerStreamReq deserializeGet(WorkerStreamReq req) {
        String key = req.getSequence() + Shared.DELIMITER + req.getMemo();
//        Object o = gzipRedisTemplate.opsForHash().get(Shared.SERIALIZATION_PARAM_HASH, key);
        try {
            ObjectSerializable<byte[]> os = new ObjectSerializable<>();
            String path = getPath();
            byte[] obj = os.deserialization(path + key);
            if (obj == null) {
                log.error("获取序列化缓存【{}】异常！", key);
                throw new RuntimeException("读取序列化缓存异常:" + key);
            }
            WorkerStreamReq cacheReq = JSON.parseObject(obj, WorkerStreamReq.class);
            return cacheReq;
        } catch (Exception e) {
            log.info("读取序列化缓存异常:", e);
            throw new RuntimeException("读取序列化缓存异常:" + key);
        }
    }

    public void clearSerializationCache(String sequence, String memo) {
        String key = sequence + Shared.DELIMITER + memo;
        String path = getPath();
//        gzipRedisTemplate.opsForHash().delete(Shared.SERIALIZATION_PARAM_HASH, key);
        File file = new File(path + key);
        if (file.exists()) {
            file.delete();
        }
        log.info("清除序列化缓存：{}", key);
    }

    public HeartbeatThread startHeartbeatThread(Object value, ActivityExecutionContext context) {
        HeartbeatThread heartbeatThread = new HeartbeatThread(heartbeatInterval, context, value);
        executorService.execute(heartbeatThread);
        return heartbeatThread;
    }
}
