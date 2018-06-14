package com.qcon.HystrixSpringExample;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    private String Id;
    private String Name;
}
