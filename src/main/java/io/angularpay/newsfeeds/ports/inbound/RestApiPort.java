package io.angularpay.newsfeeds.ports.inbound;

import com.fasterxml.jackson.databind.JsonNode;
import io.angularpay.newsfeeds.models.RequestStatus;
import io.angularpay.newsfeeds.models.ServiceType;

import java.util.List;
import java.util.Map;

public interface RestApiPort {
    List<JsonNode> getRequests(int page, Map<String, String> headers);
    List<JsonNode> getRequestsByServiceType(int page, List<ServiceType> services, Map<String, String> headers);
    List<JsonNode> getRequestsByStatus(int page, List<RequestStatus> statuses, Map<String, String> headers);
    List<JsonNode> getRequestsByServiceTypeAndStatus(int page, List<ServiceType> services, List<RequestStatus> statuses, Map<String, String> headers);
}
