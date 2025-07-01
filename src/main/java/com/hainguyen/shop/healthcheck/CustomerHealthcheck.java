package com.hainguyen.shop.healthcheck;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomerHealthcheck implements HealthIndicator {

    @Override
    public Health health() {
        // Implement your custom health check logic here
        try {
            Map<String, Object> healthCheckComponents = new HashMap<>();

            // local info
            String computerName = InetAddress.getLocalHost().getHostName();
            healthCheckComponents.put("computerName", String.format("computerName: %s", computerName));

            return Health.up().withDetails(healthCheckComponents).build();

        } catch (Exception e) {
            return Health.down().withDetail("Error", e.getMessage()).build();
        }

    }
}
