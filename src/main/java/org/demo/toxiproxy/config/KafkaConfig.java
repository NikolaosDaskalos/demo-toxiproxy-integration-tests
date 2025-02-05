package org.demo.toxiproxy.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.demo.toxiproxy.avro.OrderEventMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableKafka
@Configuration
public class KafkaConfig {
//    @Value("${spring.kafka.sasl_mechanism}")
//    protected String SASL_MECHANISM_VALUE; //= "PLAIN";

//    @Value("${spring.kafka.sasl_jaas_config}")
//    protected String saslJaasConfig;

//    @Value("${spring.kafka.security_protocol}")
//    protected String securityProtocol;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.schema-registry-url}")
    private String schemaRegistryUrl;

    @Bean
    @Profile("producer")
    public KafkaTemplate<UUID, OrderEventMessage> kafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        props.put(SaslConfigs.SASL_MECHANISM, SASL_MECHANISM_VALUE);
//        props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
//        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        props.put(ProducerConfig.RETRIES_CONFIG, "5"); // If DELIVERY_TIMEOUT_MS_CONFIG is reached first not all retries will happen
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // make sure no duplicates are sent
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000"); // Request timeout in order to retry
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "10000"); // max time to wait and retry must be > REQUEST_TIMEOUT_MS_CONFIG

//        props.put(SchemaRegistryClientConfig.BASIC_AUTH_CREDENTIALS_SOURCE, credentialSource);
//        props.put(SchemaRegistryClientConfig.USER_INFO_CONFIG, userInfoConfig);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, false);
        props.put(AbstractKafkaSchemaSerDeConfig.USE_LATEST_VERSION, true);
        props.put(AbstractKafkaSchemaSerDeConfig.LATEST_COMPATIBILITY_STRICT, false);
//        props.put(KafkaAvroSerializerConfig.FAIL_INVALID_SCHEMA, true);
        DefaultKafkaProducerFactory<UUID, OrderEventMessage> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @Profile("consumer")
    public ConcurrentKafkaListenerContainerFactory<UUID, OrderEventMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<UUID, OrderEventMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerProperties()));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private Map<String, Object> consumerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_VALUE_TYPE_CONFIG, OrderEventMessage.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return props;
    }
}