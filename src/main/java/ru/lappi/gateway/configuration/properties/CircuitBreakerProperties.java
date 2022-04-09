package ru.lappi.gateway.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author Nikita Gorodilov
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = "circuitbreaker")
public class CircuitBreakerProperties {
    @NotNull
    private Integer slidingWindowSize;
    @NotNull
    private Integer minimumNumberOfCalls;
    @NotNull
    private Integer slowCallDurationThresholdMillis;

    public Integer getSlidingWindowSize() {
        return slidingWindowSize;
    }

    public void setSlidingWindowSize(Integer slidingWindowSize) {
        this.slidingWindowSize = slidingWindowSize;
    }

    public Integer getMinimumNumberOfCalls() {
        return minimumNumberOfCalls;
    }

    public void setMinimumNumberOfCalls(Integer minimumNumberOfCalls) {
        this.minimumNumberOfCalls = minimumNumberOfCalls;
    }

    public Integer getSlowCallDurationThresholdMillis() {
        return slowCallDurationThresholdMillis;
    }

    public void setSlowCallDurationThresholdMillis(Integer slowCallDurationThresholdMillis) {
        this.slowCallDurationThresholdMillis = slowCallDurationThresholdMillis;
    }
}
