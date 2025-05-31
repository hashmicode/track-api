package com.example.tracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for the TrackNumberService.
 * verify tracking number generation logic, format, uniqueness, and constraints.
 */

public class TrackNumberServiceTest {

    private TrackNumberService service;

    @BeforeEach
    void setUp() {
        service = new TrackNumberService();
    }

    /**
     * to make sure that the generated tracking number is not null and matches the expected alphanumeric format.
     */

    @Test
    void shouldGenerateValidTrackingNumber() {
        String trackingNumber = service.generateTrackNumber(
                "MY", "ID", new BigDecimal("1.234"),
                OffsetDateTime.now(), UUID.randomUUID(),
                "RedBox Logistics", "redbox-logistics"
        );

        assertNotNull(trackingNumber);
        assertTrue(trackingNumber.matches("^[A-Z0-9]{1,16}$"), "Tracking number format invalid");
    }

    /**
     * to make sure that tracking numbers are unique across multiple calls.
     * Verifies collision resistance logic works correctly.
     */

    @Test
    void shouldGenerateUniqueTrackingNumbers() {
        String tn1 = service.generateTrackNumber(
                "MY", "ID", new BigDecimal("1.234"),
                OffsetDateTime.now(), UUID.randomUUID(),
                "RedBox Logistics", "redbox-logistics"
        );

        String tn2 = service.generateTrackNumber(
                "MY", "ID", new BigDecimal("1.234"),
                OffsetDateTime.now(), UUID.randomUUID(),
                "RedBox Logistics", "redbox-logistics"
        );

        assertNotEquals(tn1, tn2, "Tracking numbers should be unique");
    }


    /**
     * to make sure that the generated tracking number length never exceeds 16 characters.
     * This is important for downstream system constraints or barcode limitations.
     */

    @Test
    void shouldNotExceedMaxLength() {
        String trackingNumber = service.generateTrackNumber(
                "MY", "ID", new BigDecimal("1.234"),
                OffsetDateTime.now(), UUID.randomUUID(),
                "RedBox Logistics", "redbox-logistics"
        );

        assertTrue(trackingNumber.length() <= 16, "Tracking number exceeds max length");
    }
}
