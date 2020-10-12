package com.eboy.honey.oss.server.application.factory;

import com.eboy.honey.oss.constant.CallbackEnum;
import com.eboy.honey.oss.server.application.service.CallBackService;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 回调方式工厂
 *
 * @author yangzhijie
 * @date 2020/10/12 10:32
 */
public class CallbackFactory {

    private final static Map<CallbackEnum, CallBackService> SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取相应的回调服务
     *
     * @param callbackEnum 回调类型
     * @return 回调服务
     */
    public static CallBackService getCallbackService(CallbackEnum callbackEnum) {
        return SERVICE_MAP.get(callbackEnum);
    }

    /**
     * 回调服务注册
     *
     * @param callbackEnum    回调类型
     * @param callBackService 回调服务
     */
    public static void register(CallbackEnum callbackEnum, CallBackService callBackService) {
        Assert.notNull(callbackEnum, "callbackEnum must not null");
        Assert.notNull(callBackService, "callBackService must not null");
        SERVICE_MAP.put(callbackEnum, callBackService);
    }
}
