package com.qcon.HystrixSpringExample;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class CollapserController {


    @Autowired
    UserService userService;

    @RequestMapping("/getById/{id}")
    public User get(@PathVariable String id) throws ExecutionException, InterruptedException {
        HystrixRequestContext.initializeContext();
        return userService.getUserById(id);
    }


}
