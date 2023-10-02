package io.angularpay.newsfeeds.domain.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.newsfeeds.adapters.outbound.GenericFeatureServiceAdapter;
import io.angularpay.newsfeeds.exceptions.ErrorObject;
import io.angularpay.newsfeeds.models.GetNewsfeedRequestsByStatusCommandRequest;
import io.angularpay.newsfeeds.ports.outbound.GenericFeatureServicePort;
import io.angularpay.newsfeeds.validation.DefaultConstraintValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.angularpay.newsfeeds.models.ServiceType.values;

@Service
public class GetNewsfeedRequestsByStatusCommand extends AbstractCommand<GetNewsfeedRequestsByStatusCommandRequest, List<JsonNode>> {

    private final DefaultConstraintValidator validator;
    private final GenericFeatureServicePort genericFeatureServicePort;

    public GetNewsfeedRequestsByStatusCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            GenericFeatureServiceAdapter genericFeatureServicePort) {
        super("GetNewsfeedRequestsByStatusCommand", mapper);
        this.genericFeatureServicePort = genericFeatureServicePort;
        this.validator = validator;
    }

    @Override
    protected String getResourceOwner(GetNewsfeedRequestsByStatusCommandRequest request) {
        return request.getAuthenticatedUser().getUserReference();
    }

    @Override
    protected List<JsonNode> handle(GetNewsfeedRequestsByStatusCommandRequest request) {
        int page = request.getPaging().getIndex();

        Map<String, String> headers = new HashMap<>();
        headers.put("x-angularpay-username", request.getAuthenticatedUser().getUsername());
        headers.put("x-angularpay-device-id", request.getAuthenticatedUser().getDeviceId());
        headers.put("x-angularpay-correlation-id", request.getAuthenticatedUser().getCorrelationId());
        headers.put("x-angularpay-user-reference", request.getAuthenticatedUser().getUserReference());

        List<JsonNode> response = Stream.of(values())
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
    protected List<ErrorObject> validate(GetNewsfeedRequestsByStatusCommandRequest request) {
        return this.validator.validate(request);
    }
}
