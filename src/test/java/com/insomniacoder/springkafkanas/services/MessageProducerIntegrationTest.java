package com.insomniacoder.springkafkanas.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.insomniacoder.springkafkanas.models.MessagePayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@EmbeddedKafka(topics = "test",
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@SpringBootTest(properties = {"topic-name=test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageProducerIntegrationTest {

    @Autowired
    private MessageProducerImpl messageProducer;

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;

    private static String topic = "test-type1";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducerIntegrationTest.class);

    @BeforeAll
    public void setUp() {
        consumerRecords = new LinkedBlockingQueue<>();

        ContainerProperties containerProperties = new ContainerProperties(topic);

        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(
                "sender", "false", embeddedKafkaBroker);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        DefaultKafkaConsumerFactory<String, String> consumer = new DefaultKafkaConsumerFactory<>(consumerProperties);

        container = new KafkaMessageListenerContainer<>(consumer, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> {
            LOGGER.debug("Listened message='{}'", record.toString());
            consumerRecords.add(record);
        });
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterAll
    public void tearDown() {
        container.stop();
    }

    @Test
    public void whenProducerPublishShouldBeConsumedCorrectly()
            throws Exception {

        MessagePayload message = MessagePayload.builder().type("type1").message("test").build();
        String json = objectMapper.writeValueAsString(message);

        messageProducer.produceMessage(message);

        ConsumerRecord<String, String> received = consumerRecords.poll(5, TimeUnit.SECONDS);

        assertThat(received.key()).contains("type1");
        assertThat(received.value()).contains(json);
    }

    @Test
    public void whenProducerPublishIncorrectMessageTypeShouldNotGetMessage()
            throws Exception {

        MessagePayload message = MessagePayload.builder().type("type").message("test").build();

        messageProducer.produceMessage(message);

        ConsumerRecord<String, String> received = consumerRecords.poll(5, TimeUnit.SECONDS);

        assertThat(received).isNull();
    }

}