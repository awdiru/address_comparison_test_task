package ru.avdonin.address_comparison.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Info {
    private String geoLat;
    private String geoLon;
}
