package org.demo.toxiproxy.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
//    @Value("${spring.kafka.sasl_mechanism}")
//    protected String SASL_MECHANISM_VALUE; //= "PLAIN";

//    @Value("${spring.kafka.sasl_jaas_config}")
//    protected String saslJaasConfig;

//    @Value("${spring.kafka.security_protocol}")
//    protected String securityProtocol;

    @Value("${kafka.bootstrap-servers}")
    protected String bootstrapServers;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        props.put(SaslConfigs.SASL_MECHANISM, SASL_MECHANISM_VALUE);
//        props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
//        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        props.put(ProducerConfig.RETRIES_CONFIG, "5"); // If DELIVERY_TIMEOUT_MS_CONFIG is reached first not all retries will happen
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // make sure no duplicates are sent
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000"); // Request timeout in order to retry
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "10000"); // max time to wait and retry must be > REQUEST_TIMEOUT_MS_CONFIG
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }
}