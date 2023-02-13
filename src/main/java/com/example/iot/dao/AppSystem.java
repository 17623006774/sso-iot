package com.example.iot.dao;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AppSystem implements Serializable {

    private static final long serialVersionUID = -6629918430373064516L;

    private String id;

    /**
     * 系统名称
     */
    private String appName;

    /**
     * 访问key要保证唯一性
     */
    private String appKey;

    /**
     * 访问密钥
     */
    private String appSecret;

    /**
     * 每个系统的域名
     */
    private String url;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 供应商编号
     */
    private Long companyId;

}