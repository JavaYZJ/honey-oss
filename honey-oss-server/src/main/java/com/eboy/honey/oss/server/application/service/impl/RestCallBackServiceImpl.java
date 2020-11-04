package com.eboy.honey.oss.server.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.eboy.honey.oss.api.constant.CallbackEnum;
import com.eboy.honey.oss.api.entiy.CallBack;
import com.eboy.honey.oss.server.application.factory.CallbackFactory;
import com.eboy.honey.oss.server.application.service.CallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangzhijie
 * @date 2020/10/9 11:40
 */
@Primary
@Service
@Slf4j
public class RestCallBackServiceImpl implements CallBackService, InitializingBean {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 异步回调
     *
     * @param callBack 回调结果对象
     */
    @Override
    public void callBack(CallBack callBack) {
        Assert.notNull(callBack, "callBackResponse not null");
        // 添加参数
        Map<String, Object> map = new HashMap<>();
        map.put("code", callBack.getCode());
        map.put("data", callBack.getData());
        map.put("msg", callBack.getMsg());
        // 设置头信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(map, headers);
        log.info("开始执行回调，参数为：{}", JSON.toJSONString(callBack));
        // 请求
        ResponseEntity<String> rs = restTemplate.postForEntity(callBack.getCallBackHttpUrl(), requestEntity, String.class);
        log.info("RestCallBackServiceImpl 回调结果：{}", JSON.toJSONString(rs.getBody()));
    }


    @Override
    public void afterPropertiesSet() {
        CallbackFactory.register(CallbackEnum.REST, this);
    }
}
