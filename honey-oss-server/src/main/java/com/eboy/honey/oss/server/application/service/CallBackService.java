package com.eboy.honey.oss.server.application.service;

import com.eboy.honey.oss.entiy.CallBack;

/**
 * @author yangzhijie
 * @date 2020/10/9 11:37
 */
public interface CallBackService {


    /**
     * 异步回调
     *
     * @param callBack 回调结果对象
     */
    void callBack(CallBack callBack);
}
