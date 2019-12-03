package core.http.client;


import core.exception.CoreException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

@Component
public class ReactiveHttpClient {
	final static Logger logger = Logger.getLogger(ReactiveHttpClient.class);

	
	public Mono<ClientResponse> request(final WebClient.Builder builder,final String url, final String path, final HttpMethod method, Map<String, String> headers,
                                        Map<String, String> queryParams, final String jsonString) throws CoreException {

		WebClient client = builder
				.baseUrl(url)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();

		WebClient.RequestBodyUriSpec request = client.method(method);
		if(method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)){
			request
				.body(BodyInserters.fromPublisher(Mono.just(jsonString), String.class))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) ;
		}
		headers.forEach((key,value)->{
			request.header(key, value);
		});
		Function<UriBuilder,URI> func =(UriBuilder uriBuilder)->{
		    uriBuilder
			 .path(path);
			queryParams.forEach((key,value)->{
				uriBuilder.queryParam(key, value);
			});
			URI uri = uriBuilder.build();
			System.out.println(uri);
			return uri;
		};
		return request.uri(func).exchange();

	}

}
