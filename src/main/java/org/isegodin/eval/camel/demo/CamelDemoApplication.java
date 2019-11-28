package org.isegodin.eval.camel.demo;

import org.apache.camel.component.activemq.springboot.ActiveMQComponentAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = ActiveMQComponentAutoConfiguration.class)
public class CamelDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamelDemoApplication.class, args);
    }

}
