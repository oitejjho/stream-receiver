package com.simple.rabbit.streamreceiver.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Component
public class SubscriptionCompleteEvent implements Serializable {

    private String email;
    private String body;



}
