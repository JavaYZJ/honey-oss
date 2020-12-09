package red.honey.oss.job.application.job;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import red.honey.oss.job.application.service.IdentifyService;

import javax.annotation.Resource;

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
    private IdentifyService<String> pornService;


    /**
     * 图片鉴黄定时调度任务
     */
    @XxlJob("pornIdentify")
    ReturnT<String> pornIdentify(String param) {
        XxlJobLogger.log("开始执行鉴黄调度任务");
        try {
            // 查出所有未审核的图片，遍历鉴黄
            JSONObject rs = pornService.identify("");
        } catch (Exception e) {
            XxlJobLogger.log(e);
            return FAIL;
        }
        return SUCCESS;
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
