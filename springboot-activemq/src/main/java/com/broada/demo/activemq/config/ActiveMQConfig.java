package com.broada.demo.activemq.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @Author tsj
 * @Date 2020/9/9 14:48
 */
@Configuration
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String username;
    @Value("${spring.activemq.password}")
    private String password;
    @Value("${spring.activemq.queue-name}")
    private String queueName;
    @Value("${spring.activemq.topic-name}")
    private String topicName;

    @Bean(name="queue")
    public Queue getQueue(){
        return new ActiveMQQueue(queueName);
    }

    @Bean(name="topic")
    public Topic getTopic(){
        return new ActiveMQTopic(topicName);
    }

    @Bean("myConnectionFactory")
    public ConnectionFactory getConnectionFactory(){
        return new ActiveMQConnectionFactory(username,password,brokerUrl);
    }

    @Bean
    public JmsMessagingTemplate getJmsMessagingTemplate(){
        return new JmsMessagingTemplate(getConnectionFactory());
    }

    // 在Queue模式中，对消息的监听需要对containerFactory进行配置
    @Bean("queueListener")
    public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(@Qualifier("myConnectionFactory") ConnectionFactory connectionFactory){
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        return factory;
    }

    //在Topic模式中，对消息的监听需要对containerFactory进行配置
    @Bean("topicListener")
    public JmsListenerContainerFactory<?> topicJmsListenerContainerFactory(@Qualifier("myConnectionFactory") ConnectionFactory connectionFactory){
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

}
