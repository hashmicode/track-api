package com.example.tracking.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class TrackNumberService {

    private final Set<String> generatedNumbers = ConcurrentHashMap.newKeySet();

    public String generateTrackNumber(String origin, String destination, BigDecimal weight,
                                         OffsetDateTime createdAt, UUID customerId,
                                         String customerName, String customerSlug) {

        String base = (origin + destination + customerSlug.substring(0, 3) +
                createdAt.toEpochSecond() + UUID.randomUUID().toString().substring(0, 4))
                .toUpperCase().replaceAll("[^A-Z0-9]", "");

        String trackNumber = base.length() > 16 ? base.substring(0, 16) : base;

        // Ensure uniqueness (naive approach; better to use Redis or DB in real deployment)
        while (!generatedNumbers.add(trackNumber)) {
            trackNumber = new StringBuilder(trackNumber)
                    .append(new Random().nextInt(9))
                    .toString();
            trackNumber = trackNumber.length() > 16 ? trackNumber.substring(0, 16) : trackNumber;
        }


        return trackNumber;
    }
}
