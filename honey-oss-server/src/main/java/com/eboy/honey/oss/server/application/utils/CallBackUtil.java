package com.eboy.honey.oss.server.application.utils;

import com.eboy.honey.oss.entiy.CallBack;

/**
 * @author yangzhijie
 * @date 2020/10/9 14:00
 */
public class CallBackUtil {

    public static CallBack<String> buildCallback(String data, String callbackUrl, int code, String msg) {
        CallBack<String> callBack = new CallBack<>();
        callBack.setCode(code);
        callBack.setCallBackHttpUrl(callbackUrl);
        callBack.setMsg(msg);
        callBack.setData(data);
        return callBack;
    }


}
