package com.insomniacoder.springkafkanas.services.interfaces;

import com.insomniacoder.springkafkanas.models.MessagePayload;

public interface MessageProducer {

    public void produceMessage(MessagePayload payload);
}
