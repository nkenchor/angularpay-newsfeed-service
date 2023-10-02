package io.angularpay.newsfeeds.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetNewsfeedRequestsByStatusCommandRequest extends AccessControl {

    @NotNull
    @NotEmpty
    private List<RequestStatus> statuses;

    @NotNull
    @Valid
    private Paging paging;

    GetNewsfeedRequestsByStatusCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
