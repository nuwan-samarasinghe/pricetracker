package com.pricetracker.api.controllers;

import com.pricetracker.api.models.ProductTrackRequest;
import com.pricetracker.api.models.ProductTrackResponse;
import com.pricetracker.api.services.PriceTrackerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductPriceTrackerController {

    private final PriceTrackerService priceTrackerService;

    public ProductPriceTrackerController(PriceTrackerService priceTrackerService) {
        this.priceTrackerService = priceTrackerService;
    }

    @PostMapping("/track")
    public ResponseEntity<ProductTrackResponse> track(@Valid @RequestBody ProductTrackRequest trackRequest) {
        return priceTrackerService.setProductTracker(trackRequest);
    }
}
