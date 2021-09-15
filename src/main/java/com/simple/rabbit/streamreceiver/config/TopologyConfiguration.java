package com.simple.rabbit.streamreceiver.config;

import com.pivotal.rabbitmq.topology.ExchangeType;
import com.pivotal.rabbitmq.topology.TopologyBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class TopologyConfiguration {

    @Value("${queue:numbers}")
    String queueName;

    private Consumer<TopologyBuilder> topologyWithDeadLetterQueue(String name) {
        return (topologyBuilder) -> topologyBuilder
                .declareExchange(name)
                .type(ExchangeType.direct)
                .and()
                .declareQueue(name)
                    .boundTo(name);
    }

    @Bean
    public Consumer<TopologyBuilder> topology() {
        return this.topologyWithDeadLetterQueue(queueName);
    }

}
