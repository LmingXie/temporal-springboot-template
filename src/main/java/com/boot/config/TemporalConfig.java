package com.boot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 功能描述：
 *
 * @author: 代号007
 * @create: 2021-12-10 17:46
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemporalConfig {
    /**
     * 重试初始间隔
     */
    String initialInterval;
    /**
     * 重试最大间隔
     */
    String maximumInterval;
    /**
     * 重试退避系数
     */
    Integer backoffCoefficient;
    /**
     * 重试最大尝试数
     */
    Integer maximumAttempts;


    /**
     * 单次活动执行尝试的最长时间
     *
     * <p>Note that the Temporal Server doesn't detect Worker process failures directly. It relies
     * on this timeout to detect that an Activity that didn't complete on time. So this timeout
     * should be as short as the longest possible execution of the Activity body. Potentially
     * long-running Activities must specify HeartbeatTimeout and call {@link
     * io.temporal.activity.ActivityExecutionContext#heartbeat(Object)} periodically for timely failure detection.
     *
     * <p>If ScheduleToClose is not provided then this timeout is required.
     *
     * <p>Defaults to the ScheduleToCloseTimeout value.
     */
    String startToCloseTimeout;
    
    /**
     * 心跳间隔。 Activity 必须在最后一次心跳或 Activity 启动后经过此间隔之前调用 {@link io.temporal.activity.ActivityExecutionContext#heartbeat(Object)}
     * <pre>
     *     @see <a href="https://docs.temporal.io/docs/java/activities/#activity-heartbeats">活动心跳</a>
     *     对长时间运行的活动（Activity）应该适当增大心跳，否则将触发重试。
     * </pre>
     * 或者，定时响应Temporal Server活动心跳.
     */
    String heartbeatTimeout;

    /**
     * 工作流愿意等待活动完成的总时间
     *
     * <p>ScheduleToCloseTimeout 限制 Activity 执行的总时间，包括重试（使用 StartToCloseTimeout 限制单次尝试的时间）。
     *
     * <p>此选项或 StartToClose 是必需的
     *
     * <p>默认为无限制
     */
    String scheduleToCloseTimeout;


    /**
     * Activity 任务在被 Worker 拾取之前可以在任务队列中停留的时间。
     * 不要指定此超时，除非将主机特定的任务队列用于活动任务用于路由。
     *
     * <p>ScheduleToStartTimeout 始终不可重试。在此超时后重试没有意义，因为它只会将活动任务放回同一个任务队列。
     *
     * <p>默认为无限制
     */
    String scheduleToStartTimeout;

    public TemporalConfig() {
    }

    public TemporalConfig(String initialInterval, String maximumInterval, Integer backoffCoefficient, Integer maximumAttempts, String startToCloseTimeout, String heartbeatTimeout) {
        this.initialInterval = initialInterval;
        this.maximumInterval = maximumInterval;
        this.backoffCoefficient = backoffCoefficient;
        this.maximumAttempts = maximumAttempts;
        this.startToCloseTimeout = startToCloseTimeout;
        this.heartbeatTimeout = heartbeatTimeout;
    }
}
