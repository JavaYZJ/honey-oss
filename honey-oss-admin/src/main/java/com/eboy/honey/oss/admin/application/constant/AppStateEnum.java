package com.eboy.honey.oss.admin.application.constant;

import lombok.AllArgsConstructor;

/**
 * @author yangzhijie
 * @date 2020/10/13 10:16
 */
@AllArgsConstructor
public enum AppStateEnum {
    /**
     * 未启用
     */
    NOT_ENABLE(0, "未启用"),
    /**
     * 启用
     */
    ENABLE(1, "启用"),
    /**
     * 禁用
     */
    DISABLE(2, "禁用");

    private int code;

    private String desc;

    public static AppStateEnum getByCode(int code) {
        AppStateEnum[] values = AppStateEnum.values();
        for (AppStateEnum value : values) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("the code :" + code + "of the AppState not found");
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
