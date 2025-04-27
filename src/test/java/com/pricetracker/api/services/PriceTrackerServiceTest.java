package com.pricetracker.api.services;

import com.pricetracker.api.exceptions.PriceTrackerNotFoundException;
import com.pricetracker.api.models.ProductTrackRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
public class PriceTrackerServiceTest {

    @Autowired
    private PriceTrackerService priceTrackerService;

    @Test
    @DisplayName("Schedule a tracker request and desired is more than actual price")
    void scheduleTrackerWithMoreThanActualPrice(CapturedOutput output) {
        ProductTrackRequest trackRequest = new ProductTrackRequest();
        trackRequest.setUrl("https://example.com/product1");
        trackRequest.setDesiredPrice(99.99);
        trackRequest.setCheckFrequency("SECONDLY");
        priceTrackerService.setProductTracker(trackRequest);
        assertThat(output).contains("Notification: Price dropped for https://example.com/product1");
        assertThat(output).contains("Current Price: $50.0, Desired Price: $99.99");
        assertThat(output).contains("Take action now!");
    }

    @Test
    @DisplayName("Schedule a tracker request and desired is less than actual price")
    void scheduleTrackerWithLessThanActualPrice(CapturedOutput output) {
        ProductTrackRequest trackRequest = new ProductTrackRequest();
        trackRequest.setUrl("https://example.com/product1");
        trackRequest.setDesiredPrice(10.00);
        trackRequest.setCheckFrequency("SECONDLY");
        priceTrackerService.setProductTracker(trackRequest);
        assertThat(output).contains("Checking price for: https://example.com/product1 (Desired: 10.0) (Actual: 50.0)");
    }

    @Test
    @DisplayName("Schedule a tracker request with invalid product")
    void scheduleTrackerWithInvalidProduct() {
        ProductTrackRequest trackRequest = new ProductTrackRequest();
        trackRequest.setUrl("https://example.com/123");
        trackRequest.setDesiredPrice(10.00);
        trackRequest.setCheckFrequency("SECONDLY");
        assertThrows(
                PriceTrackerNotFoundException.class,
                () -> priceTrackerService.setProductTracker(trackRequest),
                "Expected to throw PriceTrackerNotFoundException for invalid product"
        );
    }
}