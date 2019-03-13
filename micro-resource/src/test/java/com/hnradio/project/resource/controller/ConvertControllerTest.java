package com.hnradio.project.resource.controller;

import com.alibaba.fastjson.JSON;
import com.hnradio.project.resource.dto.ProcessFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


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


    public void convertAideoTest() throws Exception {

        ProcessFileDTO obj = new ProcessFileDTO();

        obj.setFilePath("E:/source/周杰伦-一路向北.ape");

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


    public void convertPicTest() throws Exception {

        ProcessFileDTO obj = new ProcessFileDTO();

        obj.setFilePath("E:/source/6d67f3b53e9180c43296357ced3dee3d.jpg");
        obj.setX(200);
        obj.setY(400);
        obj.setCutHeight(600);
        obj.setCutWidth(500);
        obj.setScale(0.5f);
        obj.setOpacity(0.5f);
        obj.setWaterMarkPath("E:/source/系统流程图.png");
        obj.setPosition(1);

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
    public void test1() throws Exception {
        for(int i = 0;i < 40;i++){
            convertPicTest();
        }
    }
}