package io.angularpay.newsfeeds.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.angularpay.newsfeeds.configurations.AngularPayConfiguration;
import io.angularpay.newsfeeds.exceptions.ErrorObject;
import io.angularpay.newsfeeds.models.AccessControl;
import io.angularpay.newsfeeds.models.AuthenticatedUser;
import io.angularpay.newsfeeds.models.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.angularpay.newsfeeds.exceptions.ErrorCode.NO_PRINCIPAL;
import static io.angularpay.newsfeeds.exceptions.ErrorCode.REQUEST_NOT_FOUND;

@Slf4j
public class Helper {

    public static <T> String writeAsStringOrDefault(ObjectMapper mapper, T source) {
        try {
            return mapper.writeValueAsString(source);
        } catch (JsonProcessingException exception) {
            log.error("An error occurred while writing source parameter as string", exception);
            return "";
        }
    }

    public static AuthenticatedUser fromHeaders(Map<String, String> headers) {
        return AuthenticatedUser.builder()
                .username(headers.get("x-angularpay-username"))
                .userReference(headers.get("x-angularpay-user-reference"))
                .deviceId(headers.get("x-angularpay-device-id"))
                .correlationId(headers.get("x-angularpay-correlation-id"))
                .build();
    }

    public static HttpStatus resolveStatus(List<ErrorObject> errorObjects) {
        if (CollectionUtils.isEmpty(errorObjects)) {
            // ideally, this method shouldn't be called unless there are actual validation errors
            // this check should be performed by the caller
            // In this case the AbstractCommand already performs this check
            // Therefore, this block will never be executed
            return HttpStatus.OK;
        }
        return errorObjects.stream().anyMatch(x -> x.getCode() == NO_PRINCIPAL) ? HttpStatus.INTERNAL_SERVER_ERROR :
                errorObjects.stream().anyMatch(x -> x.getCode() == REQUEST_NOT_FOUND) ? HttpStatus.NOT_FOUND :
                        HttpStatus.BAD_REQUEST;
    }

    public static long daysUntilMaturity(String maturesOn) {
        ZonedDateTime maturityDate = Instant.parse(maturesOn).atZone(ZoneId.systemDefault());
        ZonedDateTime today = Instant.now().atZone(ZoneId.systemDefault());
        return Duration.between(today.toLocalDate().atStartOfDay(), maturityDate.toLocalDate().atStartOfDay()).toDays();
    }

    public static String getUrlByServiceType(AngularPayConfiguration configuration, ServiceType serviceType) {
        switch (serviceType) {
            case PMT:
                return configuration.getPmtUrl();
            case CTO:
                return configuration.getCryptoUrl();
            case INV:
                return configuration.getInvoiceUrl();
            case MNL:
                return configuration.getMenialUrl();
            case PCH:
                return configuration.getPitchUrl();
            case PEF:
                return configuration.getPeerfundUrl();
            case PNB:
                return configuration.getPaynableUrl();
            case SSV:
                return configuration.getSmartsaveUrl();
            case STK:
                return configuration.getStockUrl();
            case SUP:
                return configuration.getSupplyUrl();
            case ATS:
                return configuration.getAssetsUrl();
            default:
                throw new NotImplementedException("Umm.. we haven't implemented this case yet. Stay tuned!");
        }
    }

    public static void putServiceCode(List<JsonNode> nodes, ServiceType serviceType) {
        if (!CollectionUtils.isEmpty(nodes)) {
            nodes.forEach(x-> {
                ((ObjectNode)x).put("service_code", serviceType.name());
            });
        }
    }

    public static void addToMappedDiagnosticContext(String name, String value) {
        if (StringUtils.hasText(value)) {
            MDC.put(name, value);
        }
    }

    public static void addToMappedDiagnosticContextOrRandomUUID(String name, String value) {
        if (StringUtils.hasText(value)) {
            MDC.put(name, value);
        } else {
            MDC.put(name, UUID.randomUUID().toString());
        }
    }

    public static String maskUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return username;
        }
        String[] split = username.split("@");
        if (split.length > 1) {
            return split[0].charAt(0) + "*****" + split[0].charAt(split[0].length() - 1) + "@" + split[1];
        } else {
            return username.substring(0, 3) + "*****" + username.substring(username.length() - 3);
        }
    }

    public static String maskUserReference(String userReference) {
        if (!StringUtils.hasText(userReference)) {
            return userReference;
        }
        String[] split = userReference.split("@");
        if (split.length > 1) {
            return split[0].charAt(0) + "*****" + split[0].charAt(split[0].length() - 1) + "@" + split[1];
        } else {
            return userReference;
        }
    }

    public static <T extends AccessControl> String maskAuthenticatedUser(ObjectMapper mapper, T raw) {
        JsonNode node = mapper.convertValue(raw, JsonNode.class);
        JsonNode authenticatedUser = node.get("authenticatedUser");
        ((ObjectNode) authenticatedUser).put("username", maskUsername(raw.getAuthenticatedUser().getUsername()));
        ((ObjectNode) authenticatedUser).put("userReference", maskUserReference(raw.getAuthenticatedUser().getUserReference()));
        return node.toString();
    }

}
