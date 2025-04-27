package com.pricetracker.api.services.impl;

import com.pricetracker.api.handlers.PriceCheckJobHandler;
import com.pricetracker.api.exceptions.PriceTrackerException;
import com.pricetracker.api.services.NotificationService;
import com.pricetracker.api.services.PriceSchedulerService;
import com.pricetracker.api.services.ProductDetailsFetchService;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class PriceSchedulerServiceImpl implements PriceSchedulerService {

    private final Scheduler scheduler;

    private final ProductDetailsFetchService jsonDataFetch;
    private final NotificationService notificationService;

    public PriceSchedulerServiceImpl(Scheduler scheduler, ProductDetailsFetchService jsonDataFetch, NotificationService notificationService) {
        this.scheduler = scheduler;
        this.jsonDataFetch = jsonDataFetch;
        this.notificationService = notificationService;
    }

    public void schedulePriceCheck(String url, Double desiredPrice, String frequency) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(PriceCheckJobHandler.class)
                .withIdentity(UUID.randomUUID().toString(), "price-check-jobs")
                .usingJobData("url", url)
                .usingJobData("desiredPrice", desiredPrice)
                .usingJobData(new JobDataMap(Map.of("productDetailsFetchService", jsonDataFetch, "notificationService", notificationService)))
                .build();
        scheduler.scheduleJob(jobDetail, buildTrigger(jobDetail, frequency));
    }

    private Trigger buildTrigger(JobDetail jobDetail, String frequency) {
        SimpleScheduleBuilder scheduleBuilder;
        switch (frequency.toUpperCase()) {
            case "DAILY":
                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(24)
                        .repeatForever();
                break;
            case "HOURLY":
                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(1)
                        .repeatForever();
                break;
            case "SECONDLY":
                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever();
                break;
            case "MIDNIGHT":
                return TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0)) // 12:00 AM
                        .build();
            case "MORNING":
                return TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(8, 0)) // 8:00 AM
                        .build();
            default:
                throw new PriceTrackerException("Unsupported frequency: " + frequency);
        }

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(scheduleBuilder)
                .build();
    }
}
