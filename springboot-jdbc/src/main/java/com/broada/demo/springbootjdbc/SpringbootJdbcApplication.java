package com.broada.demo.springbootjdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
public class SpringbootJdbcApplication implements CommandLineRunner {

    private static final String PROP = "D:\\develop\\workspace\\dailydemo\\springboot-jdbc\\src\\main\\resources\\application.properties";
    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJdbcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*String[] beans = applicationContext.getBeanDefinitionNames();
        Stream.of(beans).forEach(System.out::println);
        Object druidDataSource = applicationContext.getBean("druidDataSource");
        System.out.println(druidDataSource);*/

        Properties properties = new Properties();
        try {
            properties.load(new FileReader(PROP));
            for(Map.Entry<Object, Object> entry:properties.entrySet()){
                System.setProperty(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
