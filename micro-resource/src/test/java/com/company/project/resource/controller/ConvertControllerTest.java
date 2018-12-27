package com.company.project.resource.controller;

import com.alibaba.fastjson.JSON;
import com.company.project.resource.dto.ProcessFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ConvertControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        //让每个测试用例启动之前都构建这样一个启动项
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void convertVideoTest() throws Exception {

        ProcessFileDTO obj = new ProcessFileDTO();

        obj.setFilePath("E:/source/这个男人来自地球.mkv");

        String processFileDTO = JSON.toJSONString(obj);
        log.info("JSON后的数据：{}",processFileDTO);
        //MockMvcRequestBuilders构建post请求
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/convert/convert_file")
                //请求编码和数据格式为json和UTF8
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                //请求的参数，为json的格式
                .content(processFileDTO))
                //期望的返回值 或者返回状态码
                .andExpect(MockMvcResultMatchers.status().isOk())
                //返回请求的字符串信息
                .andReturn().getResponse().getContentAsString();
        log.info("测试结果：{}",result);


    }

    @Test
    public void test(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                plus();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }


    public void plus(){
        int i = 0;
        System.out.println(i++);
    }
}