package com.eboy.honey.oss.api.constant;

import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/8/21 10:23
 */
public enum InputSource implements Serializable {
    /**
     * 文件路径
     */
    LOCAL_FILE_PATH(0, "local file path"),
    /**
     * 远程url
     */
    REMOTE_URL(1, "remote url"),
    /**
     * Java File
     */
    JAVA_FILE(2, "java file instance"),
    /**
     * InputStream
     */
    JAVA_INPUT_STREAM(3, "java input stream"),
    /**
     * BufferImageInputStream
     */
    BUFFER_IMAGE_STREAM(4, "buffer image input stream");


    private int code;

    private String desc;

    InputSource(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
