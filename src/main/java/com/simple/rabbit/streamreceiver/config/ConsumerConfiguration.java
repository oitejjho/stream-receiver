package com.simple.rabbit.streamreceiver.config;

import com.pivotal.rabbitmq.RabbitEndpointService;
import com.pivotal.rabbitmq.ReactiveRabbit;
import com.pivotal.rabbitmq.topology.TopologyBuilder;
import com.simple.rabbit.streamreceiver.model.entity.UserEntity;
import com.simple.rabbit.streamreceiver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Disposable;
import reactor.core.Exceptions;

import java.io.IOException;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Configuration
public class ConsumerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ConsumerConfiguration.class);
    private final Consumer<TopologyBuilder> topology;
    private final RabbitEndpointService rabbit;
    private final UserRepository userRepository;
    @Value("${queue:numbers}")
    String queueName;


    public ConsumerConfiguration(RabbitEndpointService rabbit, Consumer<TopologyBuilder> topology, UserRepository userRepository) {
        this.rabbit = rabbit;
        this.topology = topology;
        this.userRepository = userRepository;
    }

    @Bean
    public Disposable consumer() throws InterruptedException {


        CountDownLatch expectedMessage = new CountDownLatch(0);

        return rabbit
                .declareTopology(topology)
                .createTransactionalConsumerStream(queueName, String.class)
                .receive()
                .doOnNext(number -> log.info("received email : {}", number.get()))
                .doOnNext(number -> {
                    log.info("sent email : {}", number.get());
                })
                .transform(ReactiveRabbit
                        .<String>rejectWhen(RuntimeException.class, Exception.class)
                        .elseCommit())
                .subscribe(number -> expectedMessage.countDown());

    }


}
