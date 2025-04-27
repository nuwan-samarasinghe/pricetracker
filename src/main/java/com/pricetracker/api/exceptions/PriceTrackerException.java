package com.pricetracker.api.exceptions;

public class PriceTrackerException extends RuntimeException {

    public PriceTrackerException(String message) {
        super(message);
    }

    public PriceTrackerException(String message, Throwable error) {
        super(message, error);
    }

}
