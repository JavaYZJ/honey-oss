package red.honey.oss.job.application.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import red.honey.oss.api.entiy.CallBack;

/**
 * @author yangzhijie
 * @date 2020/10/9 15:52
 */
@RestController
@RequestMapping("/rest")
@Slf4j
public class CallbackController {

    @PostMapping("/callback")
    public void callback(@RequestBody CallBack callBack) {
        log.info(JSON.toJSONString(callBack));
    }
}
