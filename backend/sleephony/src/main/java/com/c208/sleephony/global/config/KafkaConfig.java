// com.c208.sleephony.global.config.KafkaConfig.java
package com.c208.sleephony.global.config;

import com.c208.sleephony.domain.sleep.dto.request.RawSequenceKafkaPayload;
import com.c208.sleephony.domain.sleep.dto.response.RawSequenceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private final ObjectMapper objectMapper;


    // 1) Producer 설정
    @Bean
    public ProducerFactory<String, RawSequenceKafkaPayload> rawProducerFactory() {
        JsonSerializer<RawSequenceKafkaPayload> serializer = new JsonSerializer<>(objectMapper);

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, RawSequenceKafkaPayload> rawKafkaTemplate() {
        return new KafkaTemplate<>(rawProducerFactory());
    }

    // 2) RawSequenceResponse용 ConsumerFactory + ListenerFactory
    @Bean
    public ConsumerFactory<String, RawSequenceResponse> rawConsumerFactory() {
        JsonDeserializer<RawSequenceResponse> deserializer =
                new JsonDeserializer<>(RawSequenceResponse.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,          "sleep-stage-raw-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RawSequenceResponse>
    rawKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RawSequenceResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(rawConsumerFactory());
        return factory;
    }
}
