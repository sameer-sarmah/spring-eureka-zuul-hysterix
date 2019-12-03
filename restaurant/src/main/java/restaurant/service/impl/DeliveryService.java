package restaurant.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.exception.CoreException;
import core.http.client.ReactiveHttpClient;
import core.models.PickupOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import restaurant.service.api.IDeliveryService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class DeliveryService implements IDeliveryService {

    @Autowired
    private ReactiveHttpClient reactiveHttpClient;

    @Value("${delivery.service}")
    private String deliveryService;

    @Autowired
    Scheduler schedular;
    
    @Autowired
    private WebClient.Builder builder;

    public Mono<Void> pickupOrder(PickupOrder pickupOrder) {
        String path = "pickup-order";
        String url = String.format("http://%s/", deliveryService);
        Map<String, String> headers = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        return Mono.create((emitter)-> {
            try {
                String jsonString = objectMapper.writeValueAsString(pickupOrder);
                Mono<ClientResponse> responseMono = reactiveHttpClient.request(builder,url, path, HttpMethod.POST, headers, queryParams, jsonString);
                responseMono.subscribeOn(schedular);
                Consumer<ClientResponse> onSuccess = (ClientResponse clientResponse) -> {
                    clientResponse.bodyToMono(String.class).subscribe((json) -> {
                        System.out.println(json);
                        emitter.success();
                    });
                };
                Consumer<Throwable> onError = (exception) -> {
                    exception.printStackTrace();
                    emitter.error(exception);
                };
                responseMono.subscribe(onSuccess, onError);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (CoreException e) {
                e.printStackTrace();
            }
        });
    }

}
