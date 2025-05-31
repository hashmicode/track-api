package com.example.tracking.model;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrackNumberResponse {

    @JsonProperty("tracking_number")
    private final String trackingNumber;

    @JsonProperty("created_at")
    private final OffsetDateTime createdAt;

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
