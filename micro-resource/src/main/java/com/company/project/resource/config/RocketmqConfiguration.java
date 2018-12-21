
package com.company.project.resource.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketmqConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(RocketmqConfiguration.class);
	
	@Autowired
	private RocketmqProperties properties;
	
	@Bean
	public DefaultMQProducer accessTokenMQProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("P_ACCESS_TOKEN");
	    producer.setNamesrvAddr(properties.getNamesrvAddr());
	    producer.start();

		Map<String , Object> messageBody = new HashMap<>();
		messageBody.put("name","aaaaaaaaaa");
		String messageJSON = JSON.toJSONString(messageBody);
		String message = new String(messageJSON.getBytes(), "utf-8");
		Message msg = new Message("AccessTokenTopic", "push", String.valueOf(System.currentTimeMillis()) ,  message.getBytes());
		logger.info("**--**要发送的消息：{}",  messageBody);
		//发送消息
		producer.send(msg, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				System.out.println("MQ成功发送消息: " + sendResult);
			}
			@Override
			public void onException(Throwable e) {
				e.printStackTrace();
			}
		});

	    return producer;
	}
	
	@Bean
	public DefaultMQPushConsumer accessTokenMQConsumer() throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("C_ACCESS_TOKEN_GATEWAY");
		consumer.setNamesrvAddr(properties.getNamesrvAddr());
		consumer.setConsumeMessageBatchMaxSize(10);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("AccessTokenTopic", "*");
		consumer.setMessageModel(MessageModel.CLUSTERING); //集群消费模式，一个消息被消费后，不会再被别的节点消费
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				 try {
	                    for (MessageExt messageExt : msgs) {
	                        String messageBody = new String(messageExt.getBody(), "utf-8");
	                        System.out.println("gateway 消费消息：Msg: " + messageExt.getMsgId() + ",msgBody: " + messageBody);//输出消息内容
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
	                }
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
			}
        });
        consumer.start();
	    return consumer;
	}

	@Bean
	public DefaultMQPushConsumer accessTokenMQConsumer1() throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("C_ACCESS_TOKEN_GATEWAY1");
		consumer.setNamesrvAddr(properties.getNamesrvAddr());
		consumer.setConsumeMessageBatchMaxSize(10);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("AccessTokenTopic", "*");
		consumer.setMessageModel(MessageModel.CLUSTERING); //集群消费模式
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				 try {
	                    for (MessageExt messageExt : msgs) {
	                        String messageBody = new String(messageExt.getBody(), "utf-8");
	                        System.out.println("gateway1 消费消息：Msg: " + messageExt.getMsgId() + ",msgBody: " + messageBody);//输出消息内容
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
	                }
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
			}
        });
        consumer.start();
	    return consumer;
	}
}
