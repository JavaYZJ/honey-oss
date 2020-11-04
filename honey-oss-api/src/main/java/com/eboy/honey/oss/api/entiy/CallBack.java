package com.eboy.honey.oss.api.entiy;

/**
 * @author yangzhijie
 * @date 2020/10/9 13:43
 */
public class CallBack {

    private int code;

    private String msg;

    private Object data;

    private String callBackHttpUrl;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCallBackHttpUrl() {
        return callBackHttpUrl;
    }

    public void setCallBackHttpUrl(String callBackHttpUrl) {
        this.callBackHttpUrl = callBackHttpUrl;
    }
}
