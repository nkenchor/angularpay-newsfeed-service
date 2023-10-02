package io.angularpay.newsfeeds.adapters.outbound.mocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.angularpay.newsfeeds.models.RequestStatus;
import io.angularpay.newsfeeds.models.ServiceType;
import io.angularpay.newsfeeds.ports.outbound.GenericFeatureServicePort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static io.angularpay.newsfeeds.helpers.Helper.putServiceCode;

@Service
public class GenericFeatureServiceAdapterMock implements GenericFeatureServicePort {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<JsonNode> getRequests(int page, ServiceType serviceType, Map<String, String> headers) {
        try {
            String response = getByServiceType(serviceType);
            List<JsonNode> serviceRequests = mapper.readValue(response, new TypeReference<List<JsonNode>>(){});
            putServiceCode(serviceRequests, serviceType);
            return serviceRequests;
        } catch (JsonProcessingException exception) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<JsonNode> getRequestsByStatus(int page, ServiceType serviceType, List<RequestStatus> statuses, Map<String, String> headers) {
        try {
            String response = getByServiceType(serviceType);
            List<JsonNode> serviceRequests = mapper.readValue(response, new TypeReference<List<JsonNode>>(){});
            randomizeStatus(serviceRequests);
            List<JsonNode> filtered = serviceRequests.stream()
                    .filter(x -> statuses.contains(RequestStatus.valueOf(x.get("status").asText("ACTIVE"))))
                    .collect(Collectors.toList());
            putServiceCode(filtered, serviceType);
            return filtered;
        } catch (JsonProcessingException exception) {
            return Collections.emptyList();
        }
    }


    private static void randomizeStatus(List<JsonNode> nodes) {
        if (!CollectionUtils.isEmpty(nodes)) {
            nodes.forEach(x-> {
                int random = new Random().nextInt(RequestStatus.values().length);
                ((ObjectNode)x).put("status", RequestStatus.values()[random].name());
            });
        }
    }

    private String getByServiceType(ServiceType serviceType) {
        switch (serviceType) {
            case PMT:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@pmtsep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"PMT\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"verified\": true,\n" +
                        "        \"verified_on\": \"2021-08-30T07:40:00\",\n" +
                        "        \"amount\": {\n" +
                        "            \"currency\": \"USD\",\n" +
                        "            \"value\": \"10000\"\n" +
                        "        },\n" +
                        "        \"exchange_rate\": {\n" +
                        "            \"from\": \"USD\",\n" +
                        "            \"to\": \"NGN\",\n" +
                        "            \"rate\": \"500\",\n" +
                        "            \"date\": \"2018-02-13\",\n" +
                        "            \"type\": \"INVESTEE_RATE|BARGAIN_RATE|OFFICIAL_RATE\"\n" +
                        "        },\n" +
                        "        \"investee\": {\n" +
                        "            \"user_reference\": \"836db867-98bb-4a1f-aa9e-9db76eac584a\"\n" +
                        "        },\n" +
                        "        \"investors\": [\n" +
                        "            {\n" +
                        "                \"reference\": \"1fdecf7a-fe4d-4273-b36e-0340e8535a5f\",\n" +
                        "                \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                \"amount\": {\n" +
                        "                    \"currency\": \"USD\",\n" +
                        "                    \"value\": \"2000\"\n" +
                        "                },\n" +
                        "                \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"is_deleted\": false,\n" +
                        "                \"deleted_by\": \"INVESTOR|TTL_SERVICE|PLATFORM\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"bargain\": {\n" +
                        "            \"offers\": [\n" +
                        "                {\n" +
                        "                    \"reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\",\n" +
                        "                    \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                    \"status\": \"PENDING|ACCEPTED|REJECTED\",\n" +
                        "                    \"exchange_rate\": {\n" +
                        "                        \"from\": \"USD\",\n" +
                        "                        \"to\": \"NGN\",\n" +
                        "                        \"rate\": \"510\"\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"accepted_bargain_reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case CTO:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@ctosep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"CTO\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"amount\": {\n" +
                        "            \"currency\": \"BTC\",\n" +
                        "            \"value\": 2\n" +
                        "        },\n" +
                        "        \"exchange_rate\": {\n" +
                        "            \"from\": \"BTC\",\n" +
                        "            \"to\": \"USD\",\n" +
                        "            \"rate\": \"52000\",\n" +
                        "            \"date\": \"2018-02-13\",\n" +
                        "            \"type\": \"INVESTEE_RATE|BARGAIN_RATE|OFFICIAL_RATE\"\n" +
                        "        },\n" +
                        "        \"investee\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"investors\": [\n" +
                        "            {\n" +
                        "                \"reference\": \"1fdecf7a-fe4d-4273-b36e-0340e8535a5f\",\n" +
                        "                \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                \"amount\": {\n" +
                        "                    \"currency\": \"USD\",\n" +
                        "                    \"value\": \"32000\"\n" +
                        "                },\n" +
                        "                \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"is_deleted\": false,\n" +
                        "                \"deleted_by\": \"INVESTOR|TTL_SERVICE|PLATFORM\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"bargain\": {\n" +
                        "            \"offers\": [\n" +
                        "                {\n" +
                        "                    \"reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\",\n" +
                        "                    \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                    \"status\": \"PENDING|ACCEPTED|REJECTED\",\n" +
                        "                    \"exchange_rate\": {\n" +
                        "                        \"from\": \"BTC\",\n" +
                        "                        \"to\": \"USD\",\n" +
                        "                        \"rate\": \"42000\"\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"accepted_bargain_reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case INV:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@invsep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"INV\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"last_modified\": \"2021-08-27T07:40:00\",\n" +
                        "        \"summary\": \"Purchased TV and stuff\",\n" +
                        "        \"invoice\": {\n" +
                        "            \"items\": [\n" +
                        "                {\n" +
                        "                    \"description\": \"TV\",\n" +
                        "                    \"quantity\": 2,\n" +
                        "                    \"unit_price\": {\n" +
                        "                        \"currency\": \"USD\",\n" +
                        "                        \"value\": \"1000\"\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"sub_total\": {\n" +
                        "                \"currency\": \"USD\",\n" +
                        "                \"value\": \"2000\"\n" +
                        "            }\n" +
                        "        },\n" +
                        "        \"merchant\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"customer\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case MNL:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@mnlsep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"MNL\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"summary\": \"Need a mechanic to fix my car\",\n" +
                        "        \"amount\": {\n" +
                        "            \"currency\": \"NGN\",\n" +
                        "            \"value\": \"15000\"\n" +
                        "        },\n" +
                        "        \"service_client\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "            \"bank_account_reference\": \"1f1e196e-e869-42a0-9e1e-2676e6dc55dc\"\n" +
                        "        },\n" +
                        "        \"service_providers\": [\n" +
                        "            {\n" +
                        "                \"reference\": \"1fdecf7a-fe4d-4273-b36e-0340e8535a5f\",\n" +
                        "                \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"is_deleted\": false,\n" +
                        "                \"deleted_by\": \"INVESTOR|TTL_SERVICE|PLATFORM\",\n" +
                        "                \"deleted_on\": \"2021-08-26T07:40:00\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"bargain\": {\n" +
                        "            \"offers\": [\n" +
                        "                {\n" +
                        "                    \"reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\",\n" +
                        "                    \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                    \"status\": \"PENDING|ACCEPTED|REJECTED\",\n" +
                        "                    \"amount\": {\n" +
                        "                        \"currency\": \"NGN\",\n" +
                        "                        \"value\": \"45000\"\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"accepted_bargain_reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case PCH:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@pthsep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"PCH\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"verified\": true,\n" +
                        "        \"summary\": \"Unicron agro-tech early-growth company wants to expand its business\",\n" +
                        "        \"amount\": {\n" +
                        "            \"currency\": \"USD\",\n" +
                        "            \"value\": \"5000000\"\n" +
                        "        },\n" +
                        "        \"equity_percent\": 5,\n" +
                        "        \"investee\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"investors\": [\n" +
                        "            {\n" +
                        "                \"reference\": \"1fdecf7a-fe4d-4273-b36e-0340e8535a5f\",\n" +
                        "                \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"is_deleted\": false,\n" +
                        "                \"deleted_by\": \"INVESTOR|TTL_SERVICE|PLATFORM\",\n" +
                        "                \"deleted_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"amount\": {\n" +
                        "                    \"currency\": \"USD\",\n" +
                        "                    \"value\": \"2000\"\n" +
                        "                }\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"bargain\": {\n" +
                        "            \"offers\": [\n" +
                        "                {\n" +
                        "                    \"reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\",\n" +
                        "                    \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                    \"status\": \"PENDING|ACCEPTED|REJECTED\",\n" +
                        "                    \"equity_percent\": 25\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"accepted_bargain_reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case PEF:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@pefsep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"PEF\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"verified\": true,\n" +
                        "        \"summary\": \"XYZ Ltd business loan request\",\n" +
                        "        \"amount\": {\n" +
                        "            \"currency\": \"USD\",\n" +
                        "            \"value\": \"10000\"\n" +
                        "        },\n" +
                        "        \"interest_rate_percent\": 5,\n" +
                        "        \"matures_on\": \"2021-08-26T07:40:00Z\",\n" +
                        "        \"investee\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"investors\": [\n" +
                        "            {\n" +
                        "                \"reference\": \"1fdecf7a-fe4d-4273-b36e-0340e8535a5f\",\n" +
                        "                \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                \"amount\": {\n" +
                        "                    \"currency\": \"USD\",\n" +
                        "                    \"value\": \"2000\"\n" +
                        "                },\n" +
                        "                \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"is_deleted\": false,\n" +
                        "                \"deleted_by\": \"INVESTOR|TTL_SERVICE|PLATFORM\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"bargain\": {\n" +
                        "            \"offers\": [\n" +
                        "                {\n" +
                        "                    \"reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\",\n" +
                        "                    \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                    \"status\": \"PENDING|ACCEPTED|REJECTED\",\n" +
                        "                    \"interest_rate_percent\": 10\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"accepted_bargain_reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case PNB:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@pnbsep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"PNB\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"verified\": true,\n" +
                        "        \"verified_on\": \"2021-08-30T07:40:00\",\n" +
                        "        \"amount\": {\n" +
                        "            \"currency\": \"NGN\",\n" +
                        "            \"value\": \"250000\"\n" +
                        "        },\n" +
                        "        \"interest_rate_percent\": 2,\n" +
                        "        \"matures_on\": \"2021-08-26T07:40:00Z\",\n" +
                        "        \"investee\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"investors\": [\n" +
                        "            {\n" +
                        "                \"reference\": \"1fdecf7a-fe4d-4273-b36e-0340e8535a5f\",\n" +
                        "                \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                \"amount\": {\n" +
                        "                    \"currency\": \"USD\",\n" +
                        "                    \"value\": \"2000\"\n" +
                        "                },\n" +
                        "                \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"is_deleted\": false,\n" +
                        "                \"deleted_by\": \"INVESTOR|TTL_SERVICE|PLATFORM\",\n" +
                        "                \"deleted_on\": \"2021-08-26T07:40:00\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"bargain\": {\n" +
                        "            \"offers\": [\n" +
                        "                {\n" +
                        "                    \"reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\",\n" +
                        "                    \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                    \"status\": \"PENDING|ACCEPTED|REJECTED\",\n" +
                        "                    \"interest_rate_percent\": 5\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"accepted_bargain_reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case SSV:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@ssvsep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"SSV\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"spent_amount\": {\n" +
                        "            \"currency\": \"USD\",\n" +
                        "            \"value\": \"300\"\n" +
                        "        },\n" +
                        "        \"matures_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"applicable_goal\": {\n" +
                        "            \"from\": {\n" +
                        "                \"currency\": \"USD\",\n" +
                        "                \"value\": \"100\"\n" +
                        "            },\n" +
                        "            \"to\": {\n" +
                        "                \"currency\": \"USD\",\n" +
                        "                \"value\": \"500\"\n" +
                        "            },\n" +
                        "            \"save_percent\": 5\n" +
                        "        },\n" +
                        "        \"smart_save_amount\": {\n" +
                        "            \"currency\": \"USD\",\n" +
                        "            \"value\": \"15\"\n" +
                        "        },\n" +
                        "        \"investee\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case STK:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@stksep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"STK\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"verified\": true,\n" +
                        "        \"summary\": \"Invest in Tesla\",\n" +
                        "        \"unit_share_price\": {\n" +
                        "            \"currency\": \"USD\",\n" +
                        "            \"value\": \"1000\"\n" +
                        "        },\n" +
                        "        \"investee\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"investors\": [\n" +
                        "            {\n" +
                        "                \"reference\": \"1fdecf7a-fe4d-4273-b36e-0340e8535a5f\",\n" +
                        "                \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"is_deleted\": false,\n" +
                        "                \"deleted_by\": \"INVESTOR|TTL_SERVICE|PLATFORM\",\n" +
                        "                \"deleted_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"amount\": {\n" +
                        "                    \"currency\": \"USD\",\n" +
                        "                    \"value\": \"55000\"\n" +
                        "                }\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            case SUP:
                return "[\n" +
                        "    {\n" +
                        "        \"reference\": \"e6af6de9-e9fc-4b54-a841-7ed902bbe559\",\n" +
                        "        \"request_tag\": \"@supsep01211\",\n" +
                        "        \"service_reference\": \"988b4529-57ee-42d9-b12a-ab96c2233c92\",\n" +
                        "        \"service_code\": \"SUP\",\n" +
                        "        \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "        \"verified\": true,\n" +
                        "        \"summary\": \"ABC Contruction needs 5000 pieces of lumber supplies\",\n" +
                        "        \"commodity\": {\n" +
                        "            \"name\": \"lumber supplies\",\n" +
                        "            \"description\": \"lumber supplies\",\n" +
                        "            \"quantity\": 5000,\n" +
                        "            \"unit_price\": {\n" +
                        "                \"currency\": \"USD\",\n" +
                        "                \"value\": \"100\"\n" +
                        "            },\n" +
                        "            \"total_amount\": {\n" +
                        "                \"currency\": \"USD\",\n" +
                        "                \"value\": \"500000\"\n" +
                        "            }\n" +
                        "        },\n" +
                        "        \"buyer\": {\n" +
                        "            \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\"\n" +
                        "        },\n" +
                        "        \"suppliers\": [\n" +
                        "            {\n" +
                        "                \"reference\": \"1fdecf7a-fe4d-4273-b36e-0340e8535a5f\",\n" +
                        "                \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                \"supplying\": {\n" +
                        "                    \"quantity\": 100,\n" +
                        "                    \"total_amount\": {\n" +
                        "                        \"currency\": \"USD\",\n" +
                        "                        \"value\": \"10000\"\n" +
                        "                    }\n" +
                        "                },\n" +
                        "                \"created_on\": \"2021-08-26T07:40:00\",\n" +
                        "                \"is_deleted\": false,\n" +
                        "                \"deleted_by\": \"INVESTOR|TTL_SERVICE|PLATFORM\",\n" +
                        "                \"deleted_on\": \"2021-08-26T07:40:00\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"bargain\": {\n" +
                        "            \"offers\": [\n" +
                        "                {\n" +
                        "                    \"reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\",\n" +
                        "                    \"user_reference\": \"e40d6109-8371-4c86-a2ef-239e01866810\",\n" +
                        "                    \"status\": \"PENDING|ACCEPTED|REJECTED\",\n" +
                        "                    \"unit_price\": {\n" +
                        "                        \"currency\": \"USD\",\n" +
                        "                        \"value\": \"250\"\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"accepted_bargain_reference\": \"19c0ec36-3989-452f-b135-e3dc00bdd025\"\n" +
                        "        },\n" +
                        "        \"status\": \"ACTIVE|INACTIVE|COMPLETED|CANCELLED\"\n" +
                        "    }\n" +
                        "]";
            default:
                return "";
        }
    }
}
