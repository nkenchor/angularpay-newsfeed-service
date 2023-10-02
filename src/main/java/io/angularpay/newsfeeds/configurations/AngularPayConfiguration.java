package io.angularpay.newsfeeds.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("angularpay")
@Data
public class AngularPayConfiguration {

    private String cryptoUrl;
    private String pmtUrl;
    private String smartsaveUrl;
    private String paynableUrl;
    private String peerfundUrl;
    private String pitchUrl;
    private String supplyUrl;
    private String menialUrl;
    private String stockUrl;
    private String invoiceUrl;
    private String assetsUrl;

    private int pageSize;
    private int codecSizeInMB;
    private Redis redis;

    @Data
    public static class Redis {
        private String host;
        private int port;
        private int timeout;
    }
}
