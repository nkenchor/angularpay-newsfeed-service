package io.angularpay.newsfeeds.adapters.inbound;

import io.angularpay.newsfeeds.domain.commands.PlatformConfigurationsConverterCommand;
import io.angularpay.newsfeeds.models.platform.PlatformConfigurationIdentifier;
import io.angularpay.newsfeeds.ports.inbound.InboundMessagingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.angularpay.newsfeeds.models.platform.PlatformConfigurationSource.TOPIC;

@Service
@RequiredArgsConstructor
public class RedisMessageAdapter implements InboundMessagingPort {

    private final PlatformConfigurationsConverterCommand converterCommand;

    @Override
    public void onMessage(String message, PlatformConfigurationIdentifier identifier) {
        this.converterCommand.execute(message, identifier, TOPIC);
    }
}
