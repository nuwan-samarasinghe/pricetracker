package com.pricetracker.api.exceptions;

public class PriceTrackerNotFoundException extends RuntimeException {

    public PriceTrackerNotFoundException(String message) {
        super(message);
    }

    public PriceTrackerNotFoundException(String message, Throwable error) {
        super(message, error);
    }

}
