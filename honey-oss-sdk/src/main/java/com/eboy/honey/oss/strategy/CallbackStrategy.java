package com.eboy.honey.oss.strategy;

import com.eboy.honey.oss.api.entiy.CallBack;

/**
 * @author yangzhijie
 * @date 2020/11/10 14:25
 */
public interface CallbackStrategy {

    /**
     * 回调处理
     *
     * @param callBack 回调参数
     */
    void callbackProcess(CallBack callBack);
}
