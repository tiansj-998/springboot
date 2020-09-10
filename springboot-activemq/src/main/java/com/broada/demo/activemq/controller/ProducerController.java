package com.broada.demo.activemq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @Author tsj
 * @Date 2020/9/9 17:05
 */
@RestController
@RequestMapping("/activeMQ")
public class ProducerController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;
    @Autowired
    private Topic topic;

    @PostMapping("/queue/test")
    public String sendQueue(@RequestBody String str){
        this.sendMessage(this.queue,str);
        return "queue send message success";
    }
    @PostMapping("/topic/test")
    public String sendTopic(@RequestBody String str){
        this.sendMessage(this.topic,str);
        return "topic send message success";
    }

    public void sendMessage(Destination destination, String message){
        jmsMessagingTemplate.convertAndSend(destination,message);
    }

}
