package com.company.project.resource.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketMQConsumerTest {

    @Autowired
    RocketMQConsumer rocketMQConsumer;

    @Test
    public void convertConsumer() throws MQClientException {
        rocketMQConsumer.convertConsumer();
    }
}