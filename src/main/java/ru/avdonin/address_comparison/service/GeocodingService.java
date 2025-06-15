package ru.avdonin.address_comparison.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.avdonin.address_comparison.model.dto.AddressRequest;
import ru.avdonin.address_comparison.model.dto.ApiResponse;
import ru.avdonin.address_comparison.model.dto.ComparisonResult;
import ru.avdonin.address_comparison.model.entity.AddressComparison;
import ru.avdonin.address_comparison.repository.AddressComparisonRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GeocodingService {
    private static final Integer EARTHS_RADIUS = 6371000;

    private final DadataService dadataService;
    private final YandexService yandexService;
    private final AddressComparisonRepository repository;

    public ComparisonResult compareAddress(AddressRequest request) {
        ApiResponse yandexResponse = yandexService.getCoordinates(request.getAddress());
        ApiResponse dadataResponse = dadataService.getCoordinates(request.getAddress());

        double distance = calculateDistance(
                yandexResponse.getLatitude(), yandexResponse.getLongitude(),
                dadataResponse.getLatitude(), dadataResponse.getLongitude()
        );

        AddressComparison entity = AddressComparison.builder()
                .address(request.getAddress())
                .yandexLatitude(yandexResponse.getLatitude())
                .yandexLongitude(yandexResponse.getLongitude())
                .dadataLatitude(dadataResponse.getLatitude())
                .dadataLongitude(dadataResponse.getLongitude())
                .distance(distance)
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(entity);

        return ComparisonResult.builder()
                .yandexLat(yandexResponse.getLatitude())
                .yandexLon(yandexResponse.getLongitude())
                .dadataLat(dadataResponse.getLatitude())
                .dadataLon(dadataResponse.getLongitude())
                .distance(distance)
                .build();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTHS_RADIUS * c;
    }
}
