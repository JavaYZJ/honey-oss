package red.honey.oss.server.fileshard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import red.honey.oss.api.utils.HoneyFileUtil;
import red.honey.oss.server.application.service.FileService;

/**
 * @author yangzhijie
 * @date 2020/8/27 14:07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FileShardTest {

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;

    @Autowired
    private FileService fileService;

    @Test
    public void test() {
        String filePath = "F:\\yangzhijie520.jpeg";
        String desFile = "F:\\wangxiaocun520.jpeg";
        HoneyFileUtil.spiltFile(filePath, 3);
        HoneyFileUtil.merge(desFile, filePath, 3);

    }
}
