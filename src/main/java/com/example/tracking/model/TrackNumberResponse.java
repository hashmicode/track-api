package com.example.tracking.model;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Model class representing the API response for tracking number generation.
 * Includes the generated tracking number and the response creation timestamp.
 */
public class TrackNumberResponse {

    // serialized in JSON as "tracking_number"
    @JsonProperty("tracking_number")
    private final String trackingNumber;

    // serialized in JSON as "created_at"
    @JsonProperty("created_at")
    private final OffsetDateTime createdAt;

    /**
     * Constructs a TrackNumberResponse with tracking number and timestamp.
     *
     * @param trackingNumber the generated tracking number
     * @param createdAt the timestamp when the response was created
     */

    public TrackNumberResponse(String trackingNumber, OffsetDateTime createdAt) {
        this.trackingNumber = trackingNumber;
        this.createdAt = createdAt;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
