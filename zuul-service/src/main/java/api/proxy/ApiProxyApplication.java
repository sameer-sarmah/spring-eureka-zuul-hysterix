package api.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

import api.proxy.config.ApiProxyConfig;

@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
@Import(value = {ApiProxyConfig.class})
public class ApiProxyApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiProxyApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiProxyApplication.class, args);
        System.err.println("##########");

    }

}