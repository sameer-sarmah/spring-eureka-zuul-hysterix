package restaurant.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableDiscoveryClient
@Configuration
public class RestaurantConfig {
	@LoadBalanced
	@Bean
	public WebClient.Builder getWebClientBuilder(){
		return  WebClient.builder();
	}
}
