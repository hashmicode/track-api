package com.example.tracking.controller;

import com.example.tracking.model.TrackNumberResponse;
import com.example.tracking.service.TrackNumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TrackNumberController {

    private final TrackNumberService trackNumberService;

    public TrackNumberController(TrackNumberService trackNumberService) {
        this.trackNumberService = trackNumberService;
    }

    @GetMapping("/next-tracking-number")
    public ResponseEntity<TrackNumberResponse> getTrackingNumber(
            @RequestParam("origin_country_id") String originCountryId,
            @RequestParam("destination_country_id") String destinationCountryId,
            @RequestParam("weight") BigDecimal weight,
            @RequestParam("created_at") String createdAtRaw,
            @RequestParam("customer_id") UUID customerId,
            @RequestParam("customer_name") String customerName,
            @RequestParam("customer_slug") String customerSlug
    ) {

        OffsetDateTime createdAt;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            createdAt = OffsetDateTime.parse(createdAtRaw, formatter);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(new TrackNumberResponse("Invalid timestamp format", null));
        }


        String trackingNumber = trackNumberService.generateTrackNumber(
                originCountryId, destinationCountryId, weight, createdAt,
                customerId, customerName, customerSlug
        );


        TrackNumberResponse response = new TrackNumberResponse(trackingNumber, OffsetDateTime.now());
        return ResponseEntity.ok(response);
    }
}
