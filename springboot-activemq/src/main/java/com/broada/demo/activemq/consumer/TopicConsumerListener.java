package com.broada.demo.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Author tsj
 * @Date 2020/9/9 17:15
 */
@Component
public class TopicConsumerListener {
    /**
     *  topic模式的消费者
     * @param message
     */
    @JmsListener(destination = "${spring.activemq.topic-name}",containerFactory = "topicListener")
    public void readActiveTopic(String message){
        System.out.println("topic接受到了" + message);
    }
}
