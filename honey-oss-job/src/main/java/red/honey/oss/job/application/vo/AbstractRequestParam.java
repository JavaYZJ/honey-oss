package red.honey.oss.job.application.vo;

/**
 * @author yangzhijie
 * @date 2020/9/29 14:01
 */
public class AbstractRequestParam {

    /**
     * 应用标识（AppId）
     */
    private long app_id;

    /**
     * 请求时间戳（秒级）
     */
    private int time_stamp;

    /**
     * 随机字符串
     */
    private String nonce_str;

    /**
     * 签名信息
     */
    private String sign;

    public long getApp_id() {
        return app_id;
    }

    public void setApp_id(long app_id) {
        this.app_id = app_id;
    }

    public int getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(int time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
