package com.qcon.configdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@RefreshScope // without this annotation config value for this bean is only read one time at startup
public class ApiController {

    @Value("${DisplayMessage}")
    String displayMessage;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String helloWorld()
    {
        return displayMessage;
    }
}
