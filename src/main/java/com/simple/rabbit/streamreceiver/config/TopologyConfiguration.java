package com.simple.rabbit.streamreceiver.config;

import com.pivotal.rabbitmq.topology.TopologyBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class TopologyConfiguration {

    /*@Value("${exchange:numbers}")
    String exchangeName;*/
    @Value("${queue:numbers}")
    String queueName;

    /*@Value("${exchange:numbers-dlx}")
    String dlExchangeName;
    @Value("${queue:numbers-dlq}")
    String dlqueueName;*/


    private Consumer<TopologyBuilder> topologyWithDeadLetterQueue(String name) {
        return (topologyBuilder) -> topologyBuilder
                .declareExchange(name)
                .and()
                .declareExchange(name + ".dlx")
                .and()
                .declareQueue(name + ".dlq")
                    .boundTo(name + ".dlx")
                .and()
                .declareQueue(name)
                    .withDeadLetterExchange(name + ".dlx")
                    .boundTo(name);
    }

    @Bean
    public Consumer<TopologyBuilder> topology() {
        return this.topologyWithDeadLetterQueue(queueName);
    }

}
