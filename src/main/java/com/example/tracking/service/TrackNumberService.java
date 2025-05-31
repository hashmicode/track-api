package com.example.tracking.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class TrackNumberService {
    private static final Logger logger = LoggerFactory.getLogger(TrackNumberService.class);


    private final Set<String> generatedNumbers = ConcurrentHashMap.newKeySet();

    public String generateTrackNumber(String origin, String destination, BigDecimal weight,
                                      OffsetDateTime createdAt, UUID customerId,
                                      String customerName, String customerSlug) {
        logger.info("Generating tracking number for customer: {} (ID: {})", customerName, customerId);

        String base = (origin + destination + customerSlug.substring(0, 3) +
                createdAt.toEpochSecond() + UUID.randomUUID().toString().substring(0, 4))
                .toUpperCase().replaceAll("[^A-Z0-9]", "");

        String trackNumber = base.length() > 16 ? base.substring(0, 16) : base;

        logger.info("Initial tracking number candidate: {}", trackNumber);

        while (!generatedNumbers.add(trackNumber)) {
            logger.warn("Collision detected for tracking number: {}", trackNumber);
            trackNumber = trackNumber.substring(0, Math.min(trackNumber.length(), 15)) + new Random().nextInt(10);
            trackNumber = trackNumber.length() > 16 ? trackNumber.substring(0, 16) : trackNumber;
        }

        logger.info("Final unique tracking number generated: {}", trackNumber);
        return trackNumber;
    }

}
