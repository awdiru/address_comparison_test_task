package ru.avdonin.address_comparison.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.avdonin.address_comparison.model.dto.ApiResponse;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DadataService {
    private final WebClient dadataWebClient;

    public ApiResponse getCoordinates(String address) {
        String[] requestBody = new String[]{address};
        List<DadataAddress> addresses = dadataWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                        "Dadata API error: " + response.statusCode() + " - " + errorBody
                                ))))
                .bodyToMono(new ParameterizedTypeReference<List<DadataAddress>>() {
                })
                .block();

        if (addresses == null || addresses.isEmpty()) {
            throw new RuntimeException("The address was not found in Dadata");
        }

        DadataAddress firstAddress = addresses.get(0);
        return new ApiResponse(
                Double.parseDouble(firstAddress.getGeoLat()),
                Double.parseDouble(firstAddress.getGeoLon())
        );
    }

    @Data
    private static class DadataAddress {
        @JsonProperty("source")
        private String source;

        @JsonProperty("result")
        private String result;

        @JsonProperty("geo_lat")
        private String geoLat;

        @JsonProperty("geo_lon")
        private String geoLon;
    }
}
