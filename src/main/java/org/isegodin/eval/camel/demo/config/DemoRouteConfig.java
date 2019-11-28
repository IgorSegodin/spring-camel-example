package org.isegodin.eval.camel.demo.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author igor.segodin
 */
@Component
public class DemoRouteConfig extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("activemq:foo")
                .to("log:sample");

        from("timer:bar")
                .setBody(constant("Hello from Camel"))
                .to("activemq:foo");

    }
}
