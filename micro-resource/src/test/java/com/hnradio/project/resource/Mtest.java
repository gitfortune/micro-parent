package com.hnradio.project.resource;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Mtest {

    @Test
    public void test1(){

        String str = "xxxx/xxxx/xxxx.jpg";
        String substring = str.substring(str.lastIndexOf(".")+1);
        log.info(substring);
    }
}
