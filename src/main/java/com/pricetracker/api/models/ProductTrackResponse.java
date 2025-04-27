package com.pricetracker.api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTrackResponse {
    private String message;
}
