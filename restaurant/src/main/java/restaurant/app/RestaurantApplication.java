package restaurant.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import restaurant.config.RestaurantKafkaConfig;
import restaurant.config.RestaurantSQLConfig;

@EnableEurekaClient
@SpringBootApplication
@Import(value = {RestaurantKafkaConfig.class, RestaurantSQLConfig.class})
public class RestaurantApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestaurantApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
        System.err.println("##########");

    }

}