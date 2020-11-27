package red.honey.oss.job;

import cn.xsshome.taip.vision.TAipVision;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yangzhijie
 * @date 2020/10/9 9:43
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CommonTest {

    @Value("${honey.oss.app_id}")
    private String appId;

    @Value("${honey.oss.app_key}")
    private String appKey;

    @Test
    public void pornTest() throws Exception {
        TAipVision aipVision = new TAipVision(appId, appKey);
        String filePath = "C:\\Users\\admin\\Pictures\\Saved Pictures\\1.jpg";
        String rs = aipVision.visionPornByURL("https://cdn.ai.qq.com/aiplat/static/ai-demo/large/y-4.jpg");
        String rs1 = aipVision.visionPorn(filePath);
        log.info(JSON.toJSONString(rs));
        log.info(JSON.toJSONString(rs1));
    }
}
