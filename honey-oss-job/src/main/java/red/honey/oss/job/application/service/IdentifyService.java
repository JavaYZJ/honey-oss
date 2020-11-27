package red.honey.oss.job.application.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author yangzhijie
 * @date 2020/9/29 14:00
 */
public interface IdentifyService<T> {


    /**
     * 鉴别
     *
     * @param t 参数
     * @return JSONObject
     */
    JSONObject identify(T t);
}
