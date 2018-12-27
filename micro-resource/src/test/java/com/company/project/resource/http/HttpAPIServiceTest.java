package com.company.project.resource.http;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class HttpAPIServiceTest {

    @Autowired
    HttpAPIService httpAPIService;

    @Test
    public void doPost() throws Exception {
        HttpResult httpResult = httpAPIService.doPost("http://localhost:8090/main");
        log.info(httpResult.getCode().toString());
    }
}