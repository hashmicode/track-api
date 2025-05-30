package com.example.tracking.model;

import java.time.OffsetDateTime;

public record TrackNumberResponse(String tracking_number, OffsetDateTime created_at) {}
