package com.eboy.honey.oss.server.file;

import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yangzhijie
 * @date 2020/8/14 11:38
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FileTest {

    @Autowired
    private FileService fileService;


    @Test
    public void upload() {
        FileVo fileVo = new FileVo();

    }


}
