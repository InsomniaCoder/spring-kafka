package com.insomniacoder.springkafkanas.controllers.interfaces;

import com.insomniacoder.springkafkanas.models.MessagePayload;

public interface KafkaProducerController {

    public void sendMessage(MessagePayload messagePayload);
}
