package delivery.app;

import delivery.config.DeliveryConfig;
import delivery.config.DeliveryKafkaConfig;
import delivery.config.DeliverySQLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

@EnableEurekaClient
@SpringBootApplication
@Import(value = {DeliveryKafkaConfig.class, DeliverySQLConfig.class,DeliveryConfig.class})
public class DeliveryApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DeliveryApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
        System.err.println("##########");

    }

}