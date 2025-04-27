package com.pricetracker.api.services;

import org.quartz.*;

public interface PriceSchedulerService {
    void schedulePriceCheck(String url, Double desiredPrice, String frequency) throws SchedulerException;
}
