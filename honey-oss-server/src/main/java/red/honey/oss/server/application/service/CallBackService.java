package red.honey.oss.server.application.service;

import red.honey.oss.api.entiy.CallBack;

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
