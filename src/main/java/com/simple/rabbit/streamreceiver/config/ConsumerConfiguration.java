package com.simple.rabbit.streamreceiver.config;

import com.pivotal.rabbitmq.RabbitEndpointService;
import com.pivotal.rabbitmq.ReactiveRabbit;
import com.pivotal.rabbitmq.topology.TopologyBuilder;
import com.simple.rabbit.streamreceiver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Disposable;

import java.util.function.Consumer;

@Configuration
public class ConsumerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ConsumerConfiguration.class);
    private final Consumer<TopologyBuilder> topology;
    private final RabbitEndpointService rabbit;
    private final EmailService emailService;
    @Value("${queue:numbers}")
    String queueName;


    public ConsumerConfiguration(RabbitEndpointService rabbit, Consumer<TopologyBuilder> topology, UserRepository userRepository, EmailService emailService) {
        this.rabbit = rabbit;
        this.topology = topology;
        this.emailService = emailService;
    }

    @Bean
    public Disposable consumer() throws InterruptedException {

        return rabbit
                .declareTopology(topology)
                .createTransactionalConsumerStream(queueName, String.class)
                .receive()
                .doOnNext(event -> log.info("received email : {}", event.get()))
                .doOnNext(event -> {
                    log.info("email sent : {}", event.get());
                })
                .transform(ReactiveRabbit
                        .<String>rejectWhen(RuntimeException.class, Exception.class)
                        .elseCommit())
                .subscribe();

    }

}
