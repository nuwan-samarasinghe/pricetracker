package com.pricetracker.api.services.impl;

import com.pricetracker.api.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void notify(String notification) {
      log.info("Notification: {}", notification);
    }
}
