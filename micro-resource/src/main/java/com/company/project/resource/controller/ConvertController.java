package com.company.project.resource.controller;

import com.alibaba.fastjson.JSON;
import com.company.project.common.domain.RestResponse;
import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.exception.ConvertException;
import com.company.project.resource.service.ConvertService;
import com.company.project.resource.utils.CheckFileTypeUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "音视频转码服务，图片处理", tags = "ConvertApi", description="音视频转码服务，图片处理")
@Slf4j
@RequestMapping("/convert")
public class ConvertController {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @RequestMapping("/convert_file")
    public RestResponse convertFile(@RequestBody ProcessFileDTO processFileDTO){
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




