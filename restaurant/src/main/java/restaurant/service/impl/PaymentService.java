package restaurant.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.exception.CoreException;
import core.models.TransferMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import core.http.client.ReactiveHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import restaurant.service.api.IDeliveryService;
import restaurant.service.api.IPaymentService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private ReactiveHttpClient reactiveHttpClient;

    @Autowired
    Scheduler schedular;

    @Value( "${payment.service}" )
    private String paymentService;

    @Autowired
    private IDeliveryService deliveryService;
    
    @Autowired
    private WebClient.Builder builder;

    public Mono<Object> transferMoney(TransferMoney transferMoney){
        String path = "transfer-money";
        String url = String.format("http://%s/payment/", paymentService);
        Map<String, String> headers = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        return Mono.create((emitter)->{
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonString = objectMapper.writeValueAsString(transferMoney);
                Mono<ClientResponse> responseMono = reactiveHttpClient.request(builder,url,path, HttpMethod.POST,headers,queryParams,jsonString);
                responseMono.subscribeOn(schedular);
                Consumer<ClientResponse> onSuccess= (ClientResponse clientResponse) -> {
                	emitter.success(transferMoney);
                };
                Consumer<Throwable> onError = (exception)->{
                    emitter.error(exception);
                };
                responseMono.subscribe(onSuccess,onError);
            } catch (JsonProcessingException exception) {
                exception.printStackTrace();
                emitter.error(exception);
            } catch (CoreException exception) {
                exception.printStackTrace();
                emitter.error(exception);
            }
        });


    }
}
