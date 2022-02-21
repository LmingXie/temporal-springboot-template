package com.boot.temporal.po;

import com.boot.common.Shared;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 功能描述：工作流标准输入
 *
 * @program: voicecloud
 * @author: 代号007
 * @create: 2021-11-19 15:44
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkerStreamReq implements Shared {
    /**
     * 标识同一批数据
     */
    private String sequence;
    /**
     * 续传索引
     */
    private String memo;

    private Integer type;
}
