package com.eboy.honey.oss.job.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eboy.honey.oss.job.application.service.IdentifyService;
import com.eboy.honey.oss.job.application.vo.PornRequestParamParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 鉴黄服务
 *
 * @author yangzhijie
 * @date 2020/9/29 14:21
 */
@Service
public class PornServiceImpl implements IdentifyService<PornRequestParamParam> {


    @Autowired
    private RestTemplate restTemplate;

    @Value("${honey.oss.porn_url}")
    private String pornUrl;

    /**
     * 鉴别
     *
     * @param pornRequestParamParam 参数                                                                                           \n  "ret": 4096,                                                                     \n  "msg": "paramter invalid",                                                      \n  "data": {                                                                                   \n      "tag_list": [                                                                           \n        \n      ] \n  }   \n} \n"
     * @return JSONObject
     */
    @Override
    public JSONObject identify(PornRequestParamParam pornRequestParamParam) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("app_id", pornRequestParamParam.getApp_id());
        requestBody.add("time_stamp", pornRequestParamParam.getTime_stamp());
        requestBody.add("nonce_str", pornRequestParamParam.getNonce_str());
        requestBody.add("sign", pornRequestParamParam.getSign());
        requestBody.add("image_url", pornRequestParamParam.getImage_url());
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(pornUrl, requestEntity, String.class);
        return JSON.parseObject(response.getBody());
    }
}
