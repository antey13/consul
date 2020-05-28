package com.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Random;

import static java.lang.Integer.parseInt;

@Component
@ConditionalOnProperty(name = "health.random", havingValue = "true")
public class HealthCheck implements HealthIndicator {

    public static final int HEALTH_STATUS = 0;
    public static final int ERROR_STATUS = 1;

    private final Random random = new Random();

    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != HEALTH_STATUS) {
            return Health.down()
                    .withDetail("Error Code", errorCode)
                    .build();
        }
        return Health.up().build();
    }

    private int check() {
        return random.nextInt(10) > getThreshold() ? ERROR_STATUS : HEALTH_STATUS;
    }

    private Integer getThreshold() {
        return 5;
    }

}
