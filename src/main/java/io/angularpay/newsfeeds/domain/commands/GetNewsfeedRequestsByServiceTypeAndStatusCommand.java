package io.angularpay.newsfeeds.domain.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.newsfeeds.adapters.outbound.GenericFeatureServiceAdapter;
import io.angularpay.newsfeeds.exceptions.ErrorObject;
import io.angularpay.newsfeeds.models.GetNewsfeedRequestsByServiceTypeAndStatusCommandRequest;
import io.angularpay.newsfeeds.ports.outbound.GenericFeatureServicePort;
import io.angularpay.newsfeeds.validation.DefaultConstraintValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetNewsfeedRequestsByServiceTypeAndStatusCommand extends AbstractCommand<GetNewsfeedRequestsByServiceTypeAndStatusCommandRequest, List<JsonNode>> {

    private final DefaultConstraintValidator validator;
    private final GenericFeatureServicePort genericFeatureServicePort;

    public GetNewsfeedRequestsByServiceTypeAndStatusCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            GenericFeatureServiceAdapter genericFeatureServicePort) {
        super("GetNewsfeedRequestsByServiceTypeAndStatusCommand", mapper);
        this.genericFeatureServicePort = genericFeatureServicePort;
        this.validator = validator;
    }

    @Override
    protected String getResourceOwner(GetNewsfeedRequestsByServiceTypeAndStatusCommandRequest request) {
        return request.getAuthenticatedUser().getUserReference();
    }

    @Override
    protected List<JsonNode> handle(GetNewsfeedRequestsByServiceTypeAndStatusCommandRequest request) {
        int page = request.getPaging().getIndex();

        Map<String, String> headers = new HashMap<>();
        headers.put("x-angularpay-username", request.getAuthenticatedUser().getUsername());
        headers.put("x-angularpay-device-id", request.getAuthenticatedUser().getDeviceId());
        headers.put("x-angularpay-correlation-id", request.getAuthenticatedUser().getCorrelationId());
        headers.put("x-angularpay-user-reference", request.getAuthenticatedUser().getUserReference());

        List<JsonNode> response = request.getServices().stream()
                .parallel().map(value -> {
                    if (value.isEnabled()) {
                        return genericFeatureServicePort.getRequestsByStatus(page, value, request.getStatuses(), headers);
                    } else {
                        return Collections.<JsonNode>emptyList();
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Collections.shuffle(response);
        return response;
    }

    @Override
    protected List<ErrorObject> validate(GetNewsfeedRequestsByServiceTypeAndStatusCommandRequest request) {
        return this.validator.validate(request);
    }
}
