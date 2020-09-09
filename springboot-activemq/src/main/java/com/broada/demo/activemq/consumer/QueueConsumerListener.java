package com.broada.demo.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Author tsj
 * @Date 2020/9/9 17:12
 */
@Component
public class QueueConsumerListener {
    //queue模式的消费者
    @JmsListener(destination = "${spring.activemq.queue-name}",containerFactory = "queueListener")
    public void readActiveQueue(String message){
        System.out.println("queue接受到了" + message);
    }
}
