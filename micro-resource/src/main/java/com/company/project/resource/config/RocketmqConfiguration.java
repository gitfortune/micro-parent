
package com.company.project.resource.config;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.exception.ConvertException;
import com.company.project.resource.service.ConvertService;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class RocketmqConfiguration{

	private static final Logger logger = LoggerFactory.getLogger(RocketmqConfiguration.class);
	
	@Autowired
	private RocketmqProperties properties;

	@Autowired
	private ConvertService convertService;
	
	@Bean
	public DefaultMQProducer mqProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("convert_producer");
	    producer.setNamesrvAddr(properties.getNamesrvAddr());
	    producer.start();

		/*Map<String , Object> messageBody = new HashMap<>();
		messageBody.put("name","aaaaaaaaaa");
		String messageJSON = JSON.toJSONString(messageBody);
		String message = new String(messageJSON.getBytes(), "utf-8");
		Message msg = new Message("ConvertTopic", "mediaTag", String.valueOf(System.currentTimeMillis()) ,  message.getBytes());
		logger.info("**--**要发送的消息：{}",  messageBody);
		//发送消息
		producer.send(msg, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				logger.info("MQ成功发送消息: " + sendResult);
			}
			@Override
			public void onException(Throwable e) {
				e.printStackTrace();
			}
		});*/

	    return producer;
	}
	
	@Bean
	public DefaultMQPushConsumer mqConsumer() throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("convert_consumer0");
		consumer.setNamesrvAddr(properties.getNamesrvAddr());
		consumer.setConsumeMessageBatchMaxSize(10);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("ConvertTopic", "*");
		consumer.setMessageModel(MessageModel.CLUSTERING); //集群消费模式，一个消息被消费后，不会再被别的节点消费
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				 try {
	                    for (MessageExt messageExt : msgs) {
	                        String messageBody = new String(messageExt.getBody(), "utf-8");
	                        logger.info("转码服务,消费消息：Msg: " + messageExt.getMsgId() + ",msgBody: " + messageBody);//输出消息内容
							if(StringUtils.isBlank(messageBody)){
								throw new ConvertException(ResultEnmu.MQ_MESSAGE_IS_NULL);
							}
							ProcessFileDTO processFileDTO = JSON.parseObject(messageBody, ProcessFileDTO.class);
							//如果filePath中没有值，这条消息无用，返回消费成功，不再消费
							if(StringUtils.isBlank(processFileDTO.getFilePath())){
								return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
							}
							//转码处理
							convertService.consumerMsg(processFileDTO);
	                    }
	                } catch (Exception e) {
					 	logger.error("消费消息失败：{}",e.getMessage());
	                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
	                }
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
			}
        });
        consumer.start();
	    return consumer;
	}

	/*@Bean
	public DefaultMQPushConsumer mqConsumer1() throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("convert_consumer1");
		consumer.setNamesrvAddr(properties.getNamesrvAddr());
		consumer.setConsumeMessageBatchMaxSize(10);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe("ConvertTopic", "*");
		consumer.setMessageModel(MessageModel.CLUSTERING); //集群消费模式，一个消息被消费后，不会再被别的节点消费
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				try {
					for (MessageExt messageExt : msgs) {
						String messageBody = new String(messageExt.getBody(), "utf-8");
						logger.info("转码服务,消费消息1：Msg: " + messageExt.getMsgId() + ",msgBody: " + messageBody);//输出消息内容
						if(StringUtils.isBlank(messageBody)){
							throw new ConvertException(ResultEnmu.MQ_MESSAGE_IS_NULL);
						}
						ProcessFileDTO processFileDTO = JSON.parseObject(messageBody, ProcessFileDTO.class);
						//如果filePath中没有值，这条消息无用，返回消费成功，不再消费
						if(StringUtils.isBlank(processFileDTO.getFilePath())){
							return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
						}
						//转码处理
						convertService.consumerMsg(processFileDTO);
					}
				} catch (Exception e) {
					logger.error("消费消息失败：{}",e.getMessage());
					return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
			}
		});
		consumer.start();
		return consumer;
	}*/

}
