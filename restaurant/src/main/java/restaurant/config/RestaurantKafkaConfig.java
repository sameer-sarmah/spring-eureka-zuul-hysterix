package restaurant.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import core.deserailizer.DeliveryEventDeserializer;
import core.deserailizer.PaymentEventDeserializer;
import core.serializer.KafkaJsonSerializer;

@EnableKafka
@PropertySource("kafka.properties")
@Configuration
@ComponentScan(basePackages = {"core", "restaurant"})
public class RestaurantKafkaConfig {
    @Value(value = "${kafka.bootstrap-server}")
    private String bootstrapAddress;

    @Value(value = "${kafka.restaurant.command.topic}")
    private String restaurantCommandTopic;

    @Value(value = "${kafka.restaurant.event.topic}")
    private String restaurantEventTopic;

    @Value(value = "${kafka.delivery.command.topic}")
    private String deliveryCommandTopic;

    @Value(value = "${kafka.delivery.event.topic}")
    private String deliveryEventTopic;

    @Value(value = "${kafka.payment.command.topic}")
    private String paymentCommandTopic;

    @Value(value = "${kafka.payment.event.topic}")
    private String paymentEventTopic;

    @Value(value = "${kafka.event.consumer.group}")
    private String eventConsumerGroup;

    @Value(value = "${kafka.command.consumer.group}")
    private String commandConsumerGroup;


    @Bean
    public ProducerFactory<Long, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Long, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public Map<String, Object> commonKafkaConsumerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, commandConsumerGroup);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "100000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 100000);
        props.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 100000);
        return Collections.unmodifiableMap(props);
    }


    @ConditionalOnMissingBean(name = "kafkaDeliveryEventListenerContainerFactory")
    @Bean
    public ConsumerFactory<Long, Object> deliveryEventConsumerFactory() {
        Map<String, Object> clonedMap = new HashMap<>(commonKafkaConsumerConfig());
        clonedMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        clonedMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DeliveryEventDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(clonedMap);
    }

   
    @ConditionalOnMissingBean(name = "kafkaPaymentEventListenerContainerFactory")
    @Bean
    public ConsumerFactory<Long, Object> paymentEventConsumerFactory() {
        Map<String, Object> clonedMap = new HashMap<>(commonKafkaConsumerConfig());
        clonedMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        clonedMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PaymentEventDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(clonedMap);
    }


    
    @Bean
    @DependsOn({"paymentEventConsumerFactory"})
    public ConcurrentKafkaListenerContainerFactory<Long, Object> kafkaPaymentEventListenerContainerFactory(@Qualifier("paymentEventConsumerFactory") ConsumerFactory<Long, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Long, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        return factory;
    }

    @Bean
    @DependsOn({"deliveryEventConsumerFactory"})
    public ConcurrentKafkaListenerContainerFactory<Long, Object> kafkaDeliveryEventListenerContainerFactory(@Qualifier("deliveryEventConsumerFactory") ConsumerFactory<Long, Object> consumerFactory ) {
        ConcurrentKafkaListenerContainerFactory<Long, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        return factory;
    }


}
