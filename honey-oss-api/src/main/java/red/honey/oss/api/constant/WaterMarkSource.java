package red.honey.oss.api.constant;

import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/8/21 10:23
 */
public enum WaterMarkSource implements Serializable {
    /**
     * 远程url
     */
    REMOTE_URL(0, "remote url"),
    /**
     * Java File
     */
    JAVA_FILE(1, "java file instance"),
    /**
     * InputStream
     */
    JAVA_INPUT_STREAM(2, "java input stream"),
    /**
     * ImageInputStream
     */
    IMAGE_INPUT_STREAM(3, "image input stream");


    private int code;

    private String desc;

    WaterMarkSource(int code, String desc) {
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
