package org.isegodin.example.spring.camel.config;

import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author igor.segodin
 */
@Configuration
public class JmsConfig {

    @Bean
    public ConnectionFactory amqpConnectionFactory() {
        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();
        jmsConnectionFactory.setRemoteURI("amqp://localhost:5672");
        jmsConnectionFactory.setUsername("admin");
        jmsConnectionFactory.setPassword("admin");

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(jmsConnectionFactory);
        pooledConnectionFactory.setMaxConnections(2);
//        pooledConnectionFactory.setExpiryTimeout();
        pooledConnectionFactory.setIdleTimeout((int) TimeUnit.MINUTES.toMillis(10));

        return pooledConnectionFactory;

    }

    @Bean
    public AMQPComponent amqpConnection() {
        AMQPComponent amqp = new AMQPComponent();
        amqp.setConnectionFactory(amqpConnectionFactory());
        return amqp;
    }
}
