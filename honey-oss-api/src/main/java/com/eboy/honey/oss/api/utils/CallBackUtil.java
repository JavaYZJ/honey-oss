package com.eboy.honey.oss.api.utils;

import com.eboy.honey.oss.api.entiy.CallBack;

/**
 * @author yangzhijie
 * @date 2020/10/9 14:00
 */
public class CallBackUtil {

    public static CallBack buildCallback(String data, String callbackUrl, int code, String msg) {
        CallBack callBack = new CallBack();
        callBack.setCode(code);
        callBack.setCallBackHttpUrl(callbackUrl);
        callBack.setMsg(msg);
        callBack.setData(data);
        return callBack;
    }


}
