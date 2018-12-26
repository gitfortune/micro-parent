package com.company.project.resource.rocketmq;

import com.company.project.resource.config.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RocketMQConsumer {

    @Autowired
    private RocketmqProperties properties;

    public DefaultMQPushConsumer convertConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("convert_consumer");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.setConsumeMessageBatchMaxSize(10);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("ConvertTopic", "push");
        consumer.setMessageModel(MessageModel.CLUSTERING); //集群消费模式，一个消息被消费后，不会再被别的节点消费
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    for (MessageExt messageExt : msgs) {
                        String messageBody = new String(messageExt.getBody(), "utf-8");
                        System.out.println("转码系统，消费消息：Msg: " + messageExt.getMsgId() + ",msgBody: " + messageBody);//输出消息内容
                    }
                } catch (Exception e) {
                    log.info("稍后再试CCCCCCCCCCCCCCCCCCC");
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
                }
                log.info("消费成功BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            }
        });
        consumer.start();
        return consumer;
    }
}
