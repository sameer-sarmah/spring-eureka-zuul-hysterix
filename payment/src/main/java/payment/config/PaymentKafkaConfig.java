package payment.config;

import core.deserailizer.DeliveryCommandDeserializer;
import core.deserailizer.DeliveryEventDeserializer;
import core.deserailizer.PaymentCommandDeserializer;
import core.deserailizer.PaymentEventDeserializer;
import core.serializer.KafkaJsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@EnableScheduling
@PropertySource("kafka.properties")
@Configuration
@ComponentScan(basePackages = {"core", "payment"})
public class PaymentKafkaConfig {
    @Value(value = "${kafka.bootstrap-server}")
    private String bootstrapAddress;

    @Value(value = "${kafka.command.topic}")
    private String commandTopic;

    @Value(value = "${kafka.event.topic}")
    private String eventTopic;

    @Value(value = "${kafka.event.consumer.group}")
    private String eventConsumerGroup;

    @Value(value = "${kafka.command.consumer.group}")
    private String commandConsumerGroup;

    @Autowired
    private KafkaJsonSerializer kafkaJsonSerializer;


    @Bean
    public ProducerFactory<Long, Object> eventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Long, Object> eventKafkaTemplate() {
        return new KafkaTemplate<>(eventProducerFactory());
    }

}
