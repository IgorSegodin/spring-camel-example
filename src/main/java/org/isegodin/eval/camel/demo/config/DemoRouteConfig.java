package org.isegodin.eval.camel.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        from("timer:bar?delay=5000&period=5000")
                .setBody(
                        toSafeSupplier(() -> objectMapper.writeValueAsBytes(new MessageData(LocalDateTime.now(), "Heart beat")))
                )
                .multicast()
                .parallelProcessing()
                .to(
                        "activemq:foo",
                        "amqp:queue:MyQueue"
                );


        from("activemq:foo")
//                .to("log:sample")
                .process((exchange) -> {
                    System.out.println(
                            "From " + exchange.getFromEndpoint().getEndpointUri() + ", Body: " + exchange.getMessage().getBody(String.class)
                    );
                });
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
        private final LocalDateTime eventTime;
        private final String text;
    }

}
