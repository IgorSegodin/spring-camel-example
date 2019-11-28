package org.isegodin.eval.camel.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamelDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamelDemoApplication.class, args);
    }




//    from("activemq:topic:foo?clientId=1&durableSubscriptionName=bar1").to("mock:result1");
//
//    from("activemq:topic:foo?clientId=2&durableSubscriptionName=bar2").to("mock:result2");

}
