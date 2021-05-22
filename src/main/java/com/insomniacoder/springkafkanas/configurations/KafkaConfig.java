package com.insomniacoder.springkafkanas.configurations;

import com.insomniacoder.springkafkanas.models.MessagePayload;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${topic-name}")
    private String topicName;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, MessagePayload> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, MessagePayload> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic test1() {
        return new NewTopic(topicName+"-type1", 2, (short) 1);
    }
    @Bean
    public NewTopic test2() {
        return new NewTopic(topicName+"-type2", 2, (short) 1);
    }
    @Bean
    public NewTopic test3() {
        return new NewTopic(topicName+"-type3", 2, (short) 1);
    }
    @Bean
    public NewTopic test4() {
        return new NewTopic(topicName+"-type4", 2, (short) 1);
    }
    @Bean
    public NewTopic test5() {
        return new NewTopic(topicName+"-type5", 2, (short) 1);
    }


}
