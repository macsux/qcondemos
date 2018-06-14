package com.qcon.HystrixSpringExample;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class DemoController {

    @RequestMapping("/")
    // see docs on options @ https://github.com/Netflix/Hystrix/wiki/Configuration
    @HystrixCommand(fallbackMethod = "helloFallback",
        commandProperties = {
            @HystrixProperty(name="execution.isolation.strategy", value="THREAD"),
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="1000"),
            @HystrixProperty(name="execution.timeout.enabled", value="true"),
            @HystrixProperty(name="execution.isolation.thread.interruptOnTimeout", value="true"),
            @HystrixProperty(name="execution.isolation.semaphore.maxConcurrentRequests", value="10"),
            @HystrixProperty(name="fallback.isolation.semaphore.maxConcurrentRequests", value="10"),
            @HystrixProperty(name="fallback.enabled", value="true"),
            @HystrixProperty(name="circuitBreaker.enabled", value="true"),
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="20"),
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="5000"),
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="50"),
            @HystrixProperty(name="circuitBreaker.forceOpen", value="false"),
            @HystrixProperty(name="circuitBreaker.forceClosed", value="false"),
            @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="10000"),
            @HystrixProperty(name="metrics.rollingStats.numBuckets", value="10"),
            @HystrixProperty(name="metrics.rollingPercentile.enabled", value="true"),
            @HystrixProperty(name="metrics.rollingPercentile.timeInMilliseconds", value="60000"),
            @HystrixProperty(name="metrics.rollingPercentile.numBuckets", value="6"),
            @HystrixProperty(name="metrics.rollingPercentile.bucketSize", value="100"),
            @HystrixProperty(name="metrics.healthSnapshot.intervalInMilliseconds", value="500"),
            @HystrixProperty(name="requestCache.enabled", value="true"),
            @HystrixProperty(name="requestLog.enabled", value="true"),
        }
    )
    public String hello(@RequestParam(value="error", defaultValue = "false") boolean shouldError, @RequestParam(value="delay", defaultValue = "0") int delay) throws Exception
    {
        if (shouldError)
        {
            throw new RuntimeException("I'm totally not working - please fix me");
        }
        Thread.sleep(delay);
        return "Hello is always a good start";
    }


    public String helloFallback(boolean shouldError, int delay)
    {
        return "They don't make bugs like Bugs Bunny anymore";
    }
}
