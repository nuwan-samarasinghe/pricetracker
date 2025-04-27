package com.pricetracker.api.handlers;

import com.pricetracker.api.models.ProductDetail;
import com.pricetracker.api.services.NotificationService;
import com.pricetracker.api.services.ProductDetailsFetchService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;

@Slf4j
public class PriceCheckJobHandler implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();
        ProductDetailsFetchService productDetailsFetchService = (ProductDetailsFetchService) dataMap.get("productDetailsFetchService");
        NotificationService notificationService = (NotificationService) dataMap.get("notificationService");
        String url = context.getMergedJobDataMap().getString("url");
        Double desiredPrice = context.getMergedJobDataMap().getDouble("desiredPrice");
        ProductDetail productDetail = productDetailsFetchService.fetchProductDetails(url);
        log.info("Checking price for: {} (Desired: {}) (Actual: {})", url, desiredPrice, productDetail.getPrice());
        if (desiredPrice >= productDetail.getPrice()) {
            String bl = "\n 📢 Notification: Price dropped for " + url +
                    "\n 💰 Current Price: $" + productDetail.getPrice() + ", Desired Price: $" + desiredPrice +
                    "\n ✅ Take action now!";
            notificationService.notify(bl);
            try {
                Scheduler scheduler = context.getScheduler();
                String jobKey = context.getJobDetail().getKey().getName();
                scheduler.deleteJob(context.getJobDetail().getKey());
                log.info("Job {} has been unscheduled after notification", jobKey);
            } catch (Exception e) {
                log.error("Error unscheduled job: ", e);
            }
        }
    }
}