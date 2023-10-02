package io.angularpay.newsfeeds.adapters.outbound;

import com.fasterxml.jackson.databind.JsonNode;
import io.angularpay.newsfeeds.configurations.AngularPayConfiguration;
import io.angularpay.newsfeeds.models.RequestStatus;
import io.angularpay.newsfeeds.models.ServiceType;
import io.angularpay.newsfeeds.ports.outbound.GenericFeatureServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.angularpay.newsfeeds.helpers.Helper.*;

@Service
@RequiredArgsConstructor
public class GenericFeatureServiceAdapter implements GenericFeatureServicePort {

    private final WebClient webClient;
    private final AngularPayConfiguration configuration;

    @Override
    public List<JsonNode> getRequests(int page, ServiceType serviceType, Map<String, String> headers) {
        URI url = UriComponentsBuilder.fromUriString(getUrlByServiceType(configuration, serviceType))
                .path("/requests/list/newsfeed/page/")
                .path(String.valueOf(page))
                .build().toUri();

        List<JsonNode> serviceRequests = execute(headers, url);
        putServiceCode(serviceRequests, serviceType);
        return serviceRequests;
    }

    @Override
    public List<JsonNode> getRequestsByStatus(int page, ServiceType serviceType, List<RequestStatus> statuses, Map<String, String> headers) {
        URI url = UriComponentsBuilder.fromUriString(getUrlByServiceType(configuration, serviceType))
                .path("/requests/list/newsfeed/page/")
                .path(String.valueOf(page))
                .path("/filter/statuses/")
                .path(statuses.stream().map(Enum::name).collect(Collectors.joining(",")))
                .build().toUri();

        List<JsonNode> serviceRequests = execute(headers, url);
        putServiceCode(serviceRequests, serviceType);
        return serviceRequests;
    }

    private List<JsonNode> execute(Map<String, String> headers, URI url) {
        return webClient
                .get()
                .uri(url.toString())
                .header("x-angularpay-username", headers.get("x-angularpay-username"))
                .header("x-angularpay-device-id", headers.get("x-angularpay-device-id"))
                .header("x-angularpay-user-reference", headers.get("x-angularpay-user-reference"))
                .header("x-angularpay-correlation-id", headers.get("x-angularpay-correlation-id"))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(new ParameterizedTypeReference<List<JsonNode>>() {
                        });
                    } else {
                        return Mono.just(Collections.emptyList());
                    }
                })
                .block();
    }
}
