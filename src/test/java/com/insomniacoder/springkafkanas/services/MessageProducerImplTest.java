package com.insomniacoder.springkafkanas.services;

import com.insomniacoder.springkafkanas.models.MessagePayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageProducerImplTest {

    @InjectMocks
    MessageProducerImpl messageProducer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(messageProducer, "topicName", "test");
    }

    @Mock
    KafkaTemplate<String, MessagePayload> template;


    @Test
    public void producerShouldSendMessageWithKafkaTemplate(){

        MessagePayload payload = MessagePayload.builder()
                .type("type1")
                .message("test").build();

        messageProducer.produceMessage(payload);

        verify(template).send(eq("test-type1"), eq("type1"), eq(payload));
    }

    @Test
    public void producerShouldSendMultipleMessageWithKafkaTemplate(){

        MessagePayload payload = MessagePayload.builder()
                .type("type1,type2")
                .message("test").build();

        messageProducer.produceMessage(payload);

        verify(template).send(eq("test-type1"), eq("type1"), eq(payload));
        verify(template).send(eq("test-type2"), eq("type2"), eq(payload));

    }

}