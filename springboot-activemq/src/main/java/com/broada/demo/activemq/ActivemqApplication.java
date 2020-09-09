package com.broada.demo.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
//启动消息队列
@EnableJms
public class ActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivemqApplication.class, args);
    }

}
