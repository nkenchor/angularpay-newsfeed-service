package io.angularpay.newsfeeds.adapters.inbound;

import com.fasterxml.jackson.databind.JsonNode;
import io.angularpay.newsfeeds.configurations.AngularPayConfiguration;
import io.angularpay.newsfeeds.domain.commands.GetNewsfeedRequestsByServiceTypeAndStatusCommand;
import io.angularpay.newsfeeds.domain.commands.GetNewsfeedRequestsByServiceTypeCommand;
import io.angularpay.newsfeeds.domain.commands.GetNewsfeedRequestsByStatusCommand;
import io.angularpay.newsfeeds.domain.commands.GetNewsfeedRequestsCommand;
import io.angularpay.newsfeeds.models.*;
import io.angularpay.newsfeeds.ports.inbound.RestApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.angularpay.newsfeeds.helpers.Helper.fromHeaders;

@RestController
@RequestMapping("/newsfeed/requests")
@RequiredArgsConstructor
public class RestApiAdapter implements RestApiPort {

    private final GetNewsfeedRequestsCommand getNewsfeedRequestsCommand;
    private final GetNewsfeedRequestsByServiceTypeCommand getNewsfeedRequestsByServiceTypeCommand;
    private final GetNewsfeedRequestsByStatusCommand getNewsfeedRequestsByStatusCommand;
    private final GetNewsfeedRequestsByServiceTypeAndStatusCommand getNewsfeedRequestsByServiceTypeAndStatusCommand;

    private final AngularPayConfiguration configuration;

    @GetMapping("/page/{page}")
    @ResponseBody
    @Override
    public List<JsonNode> getRequests(
            @PathVariable int page,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetNewsfeedRequestsCommandRequest getNewsfeedRequestsCommandRequest = GetNewsfeedRequestsCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .build();
        return this.getNewsfeedRequestsCommand.execute(getNewsfeedRequestsCommandRequest);
    }

    @GetMapping("/page/{page}/filter/services/{services}")
    @ResponseBody
    @Override
    public List<JsonNode> getRequestsByServiceType(
            @PathVariable int page,
            @PathVariable List<ServiceType> services,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetNewsfeedRequestsByServiceTypeCommandRequest getNewsfeedRequestsByServiceTypeCommandRequest = GetNewsfeedRequestsByServiceTypeCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .services(services)
                .build();
        return this.getNewsfeedRequestsByServiceTypeCommand.execute(getNewsfeedRequestsByServiceTypeCommandRequest);
    }

    @GetMapping("/page/{page}/filter/statuses/{statuses}")
    @ResponseBody
    @Override
    public List<JsonNode> getRequestsByStatus(
            @PathVariable int page,
            @PathVariable List<RequestStatus> statuses,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetNewsfeedRequestsByStatusCommandRequest getNewsfeedRequestsByStatusCommandRequest = GetNewsfeedRequestsByStatusCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .statuses(statuses)
                .build();
        return this.getNewsfeedRequestsByStatusCommand.execute(getNewsfeedRequestsByStatusCommandRequest);
    }

    @GetMapping("/page/{page}/filter/services/{services}/statuses/{statuses}")
    @ResponseBody
    @Override
    public List<JsonNode> getRequestsByServiceTypeAndStatus(
            @PathVariable int page,
            @PathVariable List<ServiceType> services,
            @PathVariable List<RequestStatus> statuses,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetNewsfeedRequestsByServiceTypeAndStatusCommandRequest getNewsfeedRequestsByServiceTypeAndStatusCommandRequest = GetNewsfeedRequestsByServiceTypeAndStatusCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .services(services)
                .statuses(statuses)
                .build();
        return this.getNewsfeedRequestsByServiceTypeAndStatusCommand.execute(getNewsfeedRequestsByServiceTypeAndStatusCommandRequest);
    }
}
