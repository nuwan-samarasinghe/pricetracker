package com.pricetracker.api.services;

import com.pricetracker.api.models.ProductTrackRequest;
import com.pricetracker.api.models.ProductTrackResponse;
import org.springframework.http.ResponseEntity;

public interface PriceTrackerService {
    ResponseEntity<ProductTrackResponse> setProductTracker(ProductTrackRequest trackRequest);
}
