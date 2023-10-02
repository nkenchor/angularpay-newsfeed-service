package io.angularpay.newsfeeds.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatedUser {
    private String username;
    private String userReference;
    private String deviceId;
    private String correlationId;
}
