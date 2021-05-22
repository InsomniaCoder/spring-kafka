package com.insomniacoder.springkafkanas.controllers;

import com.insomniacoder.springkafkanas.controllers.interfaces.KafkaProducerController;
import com.insomniacoder.springkafkanas.models.MessagePayload;
import com.insomniacoder.springkafkanas.services.interfaces.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/send-payload")
public class KafkaProducerControllerImpl implements KafkaProducerController {



    @Autowired
    private MessageProducer producer;

    @PostMapping
    @Override
    public void sendMessage(@Valid @RequestBody MessagePayload messagePayload) {
        log.info("Message payload : {}", messagePayload);
        producer.produceMessage(messagePayload);
    }
}
