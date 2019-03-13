package com.hnradio.project.resource.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RocketMQConsumerTest {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Test
    public void convertConsumer() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        String msg = "demo msg 测试";
        log.info("开始发送消息："+msg);
        Message sendMsg = new Message("ConvertTopic","mediaTag",msg.getBytes());
        //默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        log.info("消息发送响应信息："+sendResult.toString());
    }


}