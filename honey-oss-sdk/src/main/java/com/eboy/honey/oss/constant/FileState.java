package com.eboy.honey.oss.constant;

import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/7/31 16:35
 */
@AllArgsConstructor
public enum FileState implements Serializable {
    /**
     * 上传中
     */
    UPLOADING("上传中", 0),

    /**
     * 成功
     */
    SUCCESS("成功", 1),

    /**
     * 失败
     */
    FAIL("失败", 2);

    private String desc;

    private int stateCode;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }
}
