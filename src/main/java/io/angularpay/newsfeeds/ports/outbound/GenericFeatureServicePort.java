package io.angularpay.newsfeeds.ports.outbound;

import com.fasterxml.jackson.databind.JsonNode;
import io.angularpay.newsfeeds.models.RequestStatus;
import io.angularpay.newsfeeds.models.ServiceType;

import java.util.List;
import java.util.Map;

public interface GenericFeatureServicePort {
    List<JsonNode> getRequests(int page, ServiceType serviceType, Map<String, String> headers);
    List<JsonNode>  getRequestsByStatus(int page, ServiceType serviceType, List<RequestStatus> statuses, Map<String, String> headers);
}
