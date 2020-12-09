package red.honey.oss.job.application.service.impl;

import cn.xsshome.taip.vision.TAipVision;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import red.honey.oss.job.application.service.IdentifyService;

/**
 * 鉴黄服务
 *
 * @author yangzhijie
 * @date 2020/9/29 14:21
 */
@Service
public class PornServiceImpl implements IdentifyService<String> {


    @Autowired
    private TAipVision aipVision;

    /**
     * 鉴别
     *
     * @param fileKey 文件key                                                                                       \n  "ret": 4096,                                                                     \n  "msg": "paramter invalid",                                                      \n  "data": {                                                                                   \n      "tag_list": [                                                                           \n        \n      ] \n  }   \n} \n"
     * @return JSONObject
     */
    @Override
    public JSONObject identify(String fileKey) {
        Assert.notNull(fileKey, "fileKey不能为空");
        // 通过fileKey获取inputStream 再转换byte[]
        // 调用aipVision.visionPorn()；
        return null;
    }
}
