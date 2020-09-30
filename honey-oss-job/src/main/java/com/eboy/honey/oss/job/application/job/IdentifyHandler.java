package com.eboy.honey.oss.job.application.job;

import com.alibaba.fastjson.JSONObject;
import com.eboy.honey.oss.job.application.service.IdentifyService;
import com.eboy.honey.oss.job.application.utils.CommonUtil;
import com.eboy.honey.oss.job.application.utils.SignUtil;
import com.eboy.honey.oss.job.application.vo.PornRequestParamParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static com.xxl.job.core.biz.model.ReturnT.FAIL;
import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * @author yangzhijie
 * @date 2020/9/29 15:49
 */
@Component
@Slf4j
public class IdentifyHandler {

    @Resource(name = "pornServiceImpl")
    private IdentifyService<PornRequestParamParam> pornService;

    @Value("${honey.oss.app_id}")
    private long appId;


    /**
     * 图片鉴黄定时调度任务
     */
    @XxlJob("pornIdentify")
    ReturnT<String> pornIdentify(String param) {
        XxlJobLogger.log("开始执行鉴黄调度任务");
        try {
            JSONObject rs = pornService.identify(buildPornRequest());
        } catch (Exception e) {
            XxlJobLogger.log(e);
            return FAIL;
        }
        return SUCCESS;
    }

    private PornRequestParamParam buildPornRequest() {
        PornRequestParamParam pornRequestParamParam = new PornRequestParamParam();
        pornRequestParamParam.setApp_id(appId);
        pornRequestParamParam.setTime_stamp(CommonUtil.getSecondTimestamp(new Date()));
        pornRequestParamParam.setNonce_str(CommonUtil.get32Uid());
        pornRequestParamParam.setImage_url("https://cdn.ai.qq.com/aiplat/static/ai-demo/large/y-3.jpg");
        String sign = SignUtil.tencentAISign(pornRequestParamParam);
        pornRequestParamParam.setSign(sign);
        return pornRequestParamParam;
    }

    /**
     * 图片鉴暴定时调度任务
     */
    @XxlJob("terrorismIdentify")
    ReturnT<String> terrorismIdentify(String param) {
        return null;
    }

    /**
     * 音频鉴黄/敏感词检测定时调度任务
     */
    @XxlJob("evilAudioIdentify")
    ReturnT<String> evilAudioIdentify(String param) {
        return null;
    }

}
