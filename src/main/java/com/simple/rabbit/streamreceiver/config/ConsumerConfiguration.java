package com.simple.rabbit.streamreceiver.config;

import com.pivotal.rabbitmq.RabbitEndpointService;
import com.pivotal.rabbitmq.ReactiveRabbit;
import com.pivotal.rabbitmq.topology.TopologyBuilder;
import com.simple.rabbit.streamreceiver.model.entity.SubscriptionCompleteEvent;
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

        int totalNumbers = 10;
        int rejected = 1;
        int terminated = 1;
        int filtered = 5;
        CountDownLatch expectedMessage = new CountDownLatch(totalNumbers - rejected - terminated - filtered);
        CountDownLatch expectedError = new CountDownLatch(1);

        return rabbit
                .declareTopology(topology)
                .createTransactionalConsumerStream(queueName, SubscriptionCompleteEvent.class)
                .whenReceiveIllegalEvents()
                .alwaysReject()
                .then()
                .receive()
                .delayElements(Duration.ofSeconds(5))
                .doOnNext(number -> log.info("received number : {}", number.get()))
                .doOnNext(number -> {

                    Random r = new Random();
                    int randomInt = r.nextInt(5) + 1;
                    if(randomInt == 3) {
                        log.error("throwing runtime exception {}", number.get());
                        throw new RuntimeException();
                    }

                    /*UserEntity userEntity = new UserEntity();
                    userEntity.setCardId(UUID.randomUUID().toString());
                    userEntity.setFirstName("oitejjho");
                    userEntity.setSecondName("dutta");
                    userEntity.setType("NOTHING");
                    userEntity.setStatus(1);
                    userEntity.setLevel(1);
                    userEntity.setDateOfBirth("27-11-1991");
                    userEntity.setAge(31);
                    userRepository.save(userEntity)
                            .doOnNext(entity -> log.info("Message persisted for value {}", entity.getLevel()))
                            .subscribe();*/
                })
                .transform(ReactiveRabbit
                        .<SubscriptionCompleteEvent>rejectWhen(RuntimeException.class, Exception.class)
                        .elseCommit())
//                .doOnNext(number -> log.debug("Processed: {}", number.get()))
                .subscribe(number -> expectedMessage.countDown());

    }


}
