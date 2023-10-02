package io.angularpay.newsfeeds.ports.inbound;

import io.angularpay.newsfeeds.models.platform.PlatformConfigurationIdentifier;

public interface InboundMessagingPort {
    void onMessage(String message, PlatformConfigurationIdentifier identifier);
}
