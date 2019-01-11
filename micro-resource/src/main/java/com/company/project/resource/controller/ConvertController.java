package com.company.project.resource.controller;

import com.alibaba.fastjson.JSON;
import com.company.project.common.domain.RestResponse;
import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.exception.ConvertException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/convert")
@Api(value = "音视频转码服务，图片处理", tags = "ConvertApi", description="音视频转码服务，图片处理")
public class ConvertController {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @ApiOperation(value = "音视频转码服务调用接口", notes = "这个接口实现音视频转码，图片裁剪，加水印功能")
    @PostMapping("/convert_file")
    public RestResponse convertFile(@ApiParam(name = "processFileDTO",value = "传递参数封装的对象",required = true) @RequestBody ProcessFileDTO processFileDTO){
        if(null == processFileDTO){
            throw new ConvertException(ResultEnmu.OBJ_IS_NULL);
        }
        try {
            String msg = JSON.toJSONString(processFileDTO);

            Message sendMsg = new Message("ConvertTopic","mediaTag",String.valueOf(System.currentTimeMillis()),msg.getBytes());

            defaultMQProducer.send(sendMsg);
            log.info("消息发送成功：{}",msg);
        } catch (Exception e) {
            log.error("MQ发送消息失败：{}",e.getMessage());
            throw new ConvertException(ResultEnmu.MQ_SEND_MSG_FAIL);
        }
        return RestResponse.success();
    }

}




