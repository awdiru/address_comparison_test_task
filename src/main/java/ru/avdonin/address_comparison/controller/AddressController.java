package ru.avdonin.address_comparison.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.avdonin.address_comparison.model.dto.AddressRequest;
import ru.avdonin.address_comparison.service.GeocodingService;


@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AddressController {
    private final GeocodingService geocodingService;

    @PostMapping("/compare")
    public ResponseEntity<Object> compareAddress(@RequestBody AddressRequest request) {
        try {
            return ResponseEntity.ok(geocodingService.compareAddress(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
