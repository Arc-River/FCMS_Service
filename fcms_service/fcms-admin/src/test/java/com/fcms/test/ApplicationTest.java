package com.fcms.test;

import com.fcms.Application;
import com.fcms.common.utils.file.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author NYS
 * @date 2022/5/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {
    private static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);


    @Test
    public void tabDataTest() {
        File file = FileUtils.getNetUrlHttp("https://p1.itc.cn/q_70/images01/20210612/77c474b94c0946d4ae59dc6605e2cfb6.jpeg");
        log.info(file.getName());
    }
}
