package com.insomniacoder.springkafkanas.services;

import com.insomniacoder.springkafkanas.models.MessagePayload;
import com.insomniacoder.springkafkanas.services.interfaces.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageProducerImpl implements MessageProducer {

    @Autowired
    private KafkaTemplate<String, MessagePayload> template;

    @Value("${topic-name}")
    private String topicName;

    /**
     * send message to multiple topics based on type of messages specified in payload
     * each message to the topics will have message type as a key of message
     * @param payload
     */
    @Override
    public void produceMessage(MessagePayload payload) {

        String[] messageTypes = payload.getType().split(",");

        for (String messageType: messageTypes) {

            String topicName = this.topicName + "-" + messageType;
            log.info("publishing message to topic: {}, with message {}",topicName, payload);
            this.template.send(topicName, messageType, payload);
        }
    }
}
