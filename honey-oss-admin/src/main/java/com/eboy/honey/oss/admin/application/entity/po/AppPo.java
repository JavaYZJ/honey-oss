package com.eboy.honey.oss.admin.application.entity.po;

import lombok.Data;

import java.util.Date;

/**
 * @author yangzhijie
 * @date 2020/10/13 10:04
 */
@Data
public class AppPo {

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用状态（0--未启用 1--启用 2--禁用）
     */
    private int appState;

    /**
     * 状态理由
     */
    private String stateReason;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
