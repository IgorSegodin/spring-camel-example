package org.isegodin.example.spring.camel.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author igor.segodin
 */
@RequiredArgsConstructor
@Component
public class DemoRouteConfig extends RouteBuilder {

    private final ObjectMapper objectMapper;

    @Override
    public void configure() throws Exception {
        AtomicInteger idSequence = new AtomicInteger(0);


        from("timer:bar?delay=2000&period=2000")
                .setBody(
                        toSafeSupplier(() -> objectMapper.writeValueAsBytes(new MessageData(idSequence.incrementAndGet(), LocalDateTime.now(), "Heart beat")))
                )
                .multicast()
                .parallelProcessing()
                .to(
                        "activemq:foo",
                        "amqp:topic:MyTopic",
                        "amqp:queue:MyQueue",
                        "amqp:topic:VirtualTopic.MyVirtualTopic"
                );

        from("amqp:topic:MyTopic?clientId=1&durableSubscriptionName=bar1")
                .process((exchange) -> {
                    log("S2 Durable", exchange);
                });


        from("amqp:queue:Consumer.VS1.VirtualTopic.MyVirtualTopic")
                .process((exchange) -> {
                    log("VS1", exchange);
                });

        from("amqp:queue:Consumer.VS2.VirtualTopic.MyVirtualTopic")
                .errorHandler(deadLetterChannel("amqp:queue:Dead_VS2_MyVirtualTopic")
                        .maximumRedeliveries(3)
                        .redeliveryDelay(5000)
                )
                .process((exchange) -> {
                    throw new RuntimeException();
                });
    }

    private void log(String id, Exchange exchange) {
        System.out.println(
                "From " + String.format("%1$10s", id) +
                        ", URI " + exchange.getFromEndpoint().getEndpointUri() +
                        ", Body: " + exchange.getMessage().getBody(String.class)
        );
    }


    public interface UnsafeSupplier<T> {
        T get() throws Throwable;
    }

    @SneakyThrows
    public <T> Supplier<T> toSafeSupplier(UnsafeSupplier<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }

    @Data
    public static class MessageData {
        private final int id;
        private final LocalDateTime eventTime;
        private final String text;
    }

}
