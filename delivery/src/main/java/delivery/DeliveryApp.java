package delivery;

import delivery.config.DeliveryKafkaConfig;
import delivery.config.DeliverySQLConfig;
import delivery.entities.DeliveryAgent;
import delivery.repository.DeliveryAgentRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.exception.CoreException;
import core.http.client.ReactiveHttpClient;
enum Action{
	PICKED,DELIVERED
}
public class DeliveryApp {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(DeliveryKafkaConfig.class,DeliverySQLConfig.class);
        DeliveryAgentRepository deliveryAgentRepository = ctx.getBean(DeliveryAgentRepository.class);
        ReactiveHttpClient reactiveHttpClient =  ctx.getBean(ReactiveHttpClient.class);
        Scheduler schedular = ctx.getBean(Scheduler.class);
        //populateDeliveryAgents(deliveryAgentRepository);
        //performAction(reactiveHttpClient,schedular,Action.PICKED);
        performAction(reactiveHttpClient,schedular,Action.DELIVERED);
        Thread.sleep(1000000);
    }

    private static void populateDeliveryAgents(DeliveryAgentRepository deliveryAgentRepository){
        if(deliveryAgentRepository.findAll().isEmpty()){
            DeliveryAgent manoj= new DeliveryAgent();
            manoj.setAvailable(true);
            manoj.setContactName("Manoj");
            manoj.setPhone("2134");

            DeliveryAgent rajesh= new DeliveryAgent();
            rajesh.setAvailable(true);
            rajesh.setContactName("Rajesh");
            rajesh.setPhone("2134");
            deliveryAgentRepository.save(manoj);
            deliveryAgentRepository.save(rajesh);
            deliveryAgentRepository.flush();
        }
    }
    
    private static void performAction(ReactiveHttpClient reactiveHttpClient,Scheduler schedular,Action action)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String orderId = "46";
        String path = null;
        if(action.equals(Action.PICKED)) {
        	path = String.format("delivery/picked/%s", orderId);
        }
        else if(action.equals(Action.DELIVERED)) {
        	path = String.format("delivery/delivered/%s", orderId);
        }
        String url = "http://localhost:8082/";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        Mono<ClientResponse> responseMono;
		try {
			String jsonString= "";
			responseMono = reactiveHttpClient.request(WebClient.builder(),url, path, HttpMethod.PUT, headers, queryParams, jsonString);
	        responseMono.subscribeOn(schedular);
	        Consumer<ClientResponse> onSuccess = (ClientResponse clientResponse) -> {
	            clientResponse.bodyToMono(String.class).subscribe((json) -> {
	                System.out.println(json);
	            });
	        };
	        Consumer<Throwable> onError = (exception) -> {
	            exception.printStackTrace();
	        };
	        responseMono.subscribe(onSuccess, onError);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
