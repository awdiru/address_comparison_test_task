package ru.avdonin.address_comparison.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.avdonin.address_comparison.model.dto.ApiResponse;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class YandexService {
    private final WebClient yandexWebClient;

    public ApiResponse getCoordinates(String address) {
        return yandexWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("geocode", address)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                        "Yandex API error: " + response.statusCode() + " - " + errorBody
                                ))))
                .bodyToMono(YandexResponse.class)
                .<ApiResponse>handle((response, sink) -> {
                    if (response.getResponse().getGeoObjectCollection().getFeatureMember().isEmpty()) {
                        sink.error(new RuntimeException("The address was not found in Yandex"));
                        return;
                    }
                    String[] coords = response.getResponse().getGeoObjectCollection()
                            .getFeatureMember().get(0).getGeoObject()
                            .getPoint().getPos().split(" ");
                    sink.next(new ApiResponse(
                            Double.parseDouble(coords[1]),
                            Double.parseDouble(coords[0])
                    ));
                })
                .block();
    }

    @Data
    private static class YandexResponse {
        private Response response;
    }

    @Data
    private static class Response {
        @JsonProperty("GeoObjectCollection")
        private GeoObjectCollection geoObjectCollection;
    }

    @Data
    private static class GeoObjectCollection {
        @JsonProperty("featureMember")
        private List<FeatureMember> featureMember;
    }

    @Data
    private static class FeatureMember {
        @JsonProperty("GeoObject")
        private GeoObject geoObject;
    }

    @Data
    private static class GeoObject {
        @JsonProperty("Point")
        private Point point;
    }

    @Data
    private static class Point {
        @JsonProperty("pos")
        private String pos;
    }
}
