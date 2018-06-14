package com.qcon.HystrixSpringExample;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {

    static Logger log = Logger.getLogger("collapser");
    @HystrixCollapser(scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL, batchMethod = "getUserByIds", collapserProperties = {
            @HystrixProperty(name="timerDelayInMilliseconds", value="5000"),
    })

    public User getUserById(String id) {
        return null;
    }


    @HystrixCommand
    public List<User> getUserByIds(List<String> ids) {
        // the list of return type and paramter must have the same number of elements
        // order matters - used to establish mapping between request/response

        log.info("Collapsing " + ids.size() + " requests into 1");
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM USERS WHERE ID in (" + ids.stream().distinct().reduce((id, predicate) -> predicate + "," + id).get() + ")";
        log.info(sql);

        for (String id : ids) {
            users.add(new User(id, "Name is " + id));
        }
        return users;
    }
}
