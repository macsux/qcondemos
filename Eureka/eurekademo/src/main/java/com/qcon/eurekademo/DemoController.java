package com.qcon.eurekademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.Dictionary;
import java.util.List;

@RestController

public class DemoController {
    @Autowired
    DiscoveryClient _client;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    public DemoController(DiscoveryClient client, RestTemplate restTemplate) {
//        _client = client;
//
//    }

    @RequestMapping("")
    public DiscoveryClient clients()
    {
        return _client;
    }
    @RequestMapping("/instances")
    public List<ServiceInstance> serviceInstances(@RequestParam(value="serviceName") String serviceName)
    {
        return _client.getInstances(serviceName);
    }
    @RequestMapping("/ask")
    public String ask()  {
        try
        {
            ResponseEntity<String> result = restTemplate.exchange(
                    "http://DotNetEurekaClient/hello",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<String>() {
                    });
            return result.getBody();
        }

        catch(Exception ex)
        {
            return ex.getMessage();
        }

    }
    @RequestMapping("/hello")
    public String hello()
    {
        return "Hello .NET world from Java world";
    }
}
