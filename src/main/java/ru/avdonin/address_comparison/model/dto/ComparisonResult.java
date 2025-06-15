package ru.avdonin.address_comparison.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComparisonResult {
    private Double yandexLat;
    private Double yandexLon;
    private Double dadataLat;
    private Double dadataLon;
    private Double distance;
}
