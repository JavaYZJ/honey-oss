package red.honey.oss.strategy.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import red.honey.oss.api.entiy.CallBack;
import red.honey.oss.strategy.CallbackStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangzhijie
 * @date 2020/11/10 14:28
 */
@Slf4j
public class RestCallback implements CallbackStrategy {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 回调处理
     *
     * @param callBack 回调参数
     */
    @Override
    public void callbackProcess(CallBack callBack) {
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
}
