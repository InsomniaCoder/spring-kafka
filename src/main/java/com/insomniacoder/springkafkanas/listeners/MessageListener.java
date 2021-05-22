package com.insomniacoder.springkafkanas.listeners;

import com.insomniacoder.springkafkanas.models.MessagePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {

    @KafkaListener(topics = {"topic-type1","topic-type2","topic-type3","topic-type4","topic-type5"} , groupId = "test")
    public void listenToTopics(
            @Payload MessagePayload payload,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey) {
        log.info("Received Message: Topic: {}, Payload: {}", messageKey, payload);
    }

}
