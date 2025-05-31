package com.example.tracking.controller;

import com.example.tracking.model.TrackNumberResponse;
import com.example.tracking.service.TrackNumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TrackNumberController {

    // Regular expression for validating 2-letter uppercase country codes
    private static final Pattern COUNTRY_CODE = Pattern.compile("^[A-Z]{2}$");

    // Regular expression for validating customer slug (e.g., redbox-logistics)
    private static final Pattern SLUG = Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$");

    private final TrackNumberService trackNumberService;

    // Constructor injection for the service
    public TrackNumberController(TrackNumberService trackNumberService) {
        this.trackNumberService = trackNumberService;
    }

    // REST endpoint to get the next tracking number
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
        //validate origin and destination country codes
        if (!COUNTRY_CODE.matcher(originCountryId).matches()
                || !COUNTRY_CODE.matcher(destinationCountryId).matches()) {
            return ResponseEntity.badRequest().body(new TrackNumberResponse("Invalid country code", null));
        }

        // validate weight: must be positive and up to 3 decimal places
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0 || weight.scale() > 3) {
            return ResponseEntity.badRequest().body(new TrackNumberResponse("Invalid weight format", null));
        }

        // validate customer slug format
        if (!SLUG.matcher(customerSlug).matches()) {
            return ResponseEntity.badRequest().body(new TrackNumberResponse("Invalid customer slug", null));
        }
        // parse created_at timestamp
        OffsetDateTime createdAt;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            createdAt = OffsetDateTime.parse(createdAtRaw, formatter);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(new TrackNumberResponse("Invalid timestamp format", null));
        }

        // call service to generate a tracking number
        String trackingNumber = trackNumberService.generateTrackNumber(
                originCountryId, destinationCountryId, weight, createdAt,
                customerId, customerName, customerSlug
        );


        TrackNumberResponse response = new TrackNumberResponse(trackingNumber, OffsetDateTime.now());
        return ResponseEntity.ok(response);
    }
}
