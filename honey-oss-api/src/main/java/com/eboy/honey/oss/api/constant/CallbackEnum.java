package com.eboy.honey.oss.api.constant;

import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/10/12 10:51
 */
@AllArgsConstructor
public enum CallbackEnum implements Serializable {

    /**
     * rest http 回调
     */
    REST(0, "HTTP REST 回调");

    private int code;

    private String desc;

    public static CallbackEnum getByCode(int code) {
        CallbackEnum[] values = CallbackEnum.values();
        for (CallbackEnum value : values) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("the code of callbackEnum not found");
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
