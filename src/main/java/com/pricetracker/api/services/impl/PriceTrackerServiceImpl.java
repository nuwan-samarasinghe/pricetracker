package com.pricetracker.api.services.impl;

import com.pricetracker.api.exceptions.PriceTrackerException;
import com.pricetracker.api.models.ProductTrackRequest;
import com.pricetracker.api.models.ProductTrackResponse;
import com.pricetracker.api.services.PriceSchedulerService;
import com.pricetracker.api.services.PriceTrackerService;
import com.pricetracker.api.services.ProductDetailsFetchService;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PriceTrackerServiceImpl implements PriceTrackerService {

    private final PriceSchedulerService scheduler;
    private final ProductDetailsFetchService jsonDataFetch;

    public PriceTrackerServiceImpl(PriceSchedulerService scheduler, ProductDetailsFetchService jsonDataFetch) {
        this.scheduler = scheduler;
        this.jsonDataFetch = jsonDataFetch;
    }

    @Override
    public ResponseEntity<ProductTrackResponse> setProductTracker(ProductTrackRequest trackRequest) {
        try {
            jsonDataFetch.fetchProductDetails(trackRequest.getUrl());
            scheduler.schedulePriceCheck(trackRequest.getUrl(), trackRequest.getDesiredPrice(), trackRequest.getCheckFrequency());
        } catch (SchedulerException e) {
            throw new PriceTrackerException("An error occurred while scheduling", e);
        }
        return ResponseEntity.ok().body(ProductTrackResponse.builder().message("Successfully scheduled the price track request").build());
    }
}
