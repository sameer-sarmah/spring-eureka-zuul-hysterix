package payment.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import payment.config.PaymentKafkaConfig;
import payment.config.PaymentSQLConfig;

@EnableEurekaClient
@SpringBootApplication
@Import(value = {PaymentKafkaConfig.class, PaymentSQLConfig.class})
public class PaymentApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PaymentApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
        System.err.println("##########");

    }

}