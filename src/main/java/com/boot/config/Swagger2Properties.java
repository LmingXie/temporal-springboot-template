package com.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * description swagger2 properties
 *
 * @author wangy QQ 837195190
 * <p>Created by wangy on 2019/3/15.</p>
 */
@Data
@ConfigurationProperties(prefix = "swagger2")
public class Swagger2Properties {

    /**
     * 是否开启Swagger2
     */
    private boolean enable;

    /**
     * 扫描包
     */
    private String basePackage;

    /**
     * 页面标题
     */
    private String title;

    /**
     * 创建人
     */
    private String contactName;

    private String contactUrl;

    private String contactEmail;

    /**
     * 版本号
     */
    private String version;

    /**
     * 描述
     */
    private String description;

    /**
     * 服务URL
     */
    private String serviceUrl;

}
