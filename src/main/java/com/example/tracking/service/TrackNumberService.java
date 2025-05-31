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

/**
 * service responsible for generating unique tracking numbers based on request attributes.
 * ensures collision resistance and logs important steps in the generation process.
 */

@Service
public class TrackNumberService {
    private static final Logger logger = LoggerFactory.getLogger(TrackNumberService.class);

    // thread-safe in-memory store for tracking numbers for unique check
    private final Set<String> generatedNumbers = ConcurrentHashMap.newKeySet();

    /**
     * it generates a unique tracking number based on customer and shipment metadata.
     *
     * @param origin        2-letter origin country code
     * @param destination   2-letter destination country code
     * @param weight        shipment weight used for future extensibility
     * @param createdAt     timestamp when the request was created
     * @param customerId    unique ID of the customer
     * @param customerName  name of the customer
     * @param customerSlug  slug version of the customer name
     * @return A unique 16-character tracking number
     */

    public String generateTrackNumber(String origin, String destination, BigDecimal weight,
                                      OffsetDateTime createdAt, UUID customerId,
                                      String customerName, String customerSlug) {
        logger.info("Generating tracking number for customer: {} (ID: {})", customerName, customerId);

        // to create a base string using core identifiers and a random fragment
        String base = (origin + destination + customerSlug.substring(0, 3) +
                createdAt.toEpochSecond() + UUID.randomUUID().toString().substring(0, 4))
                .toUpperCase().replaceAll("[^A-Z0-9]", "");

        // trim to target length (16 characters max)
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
