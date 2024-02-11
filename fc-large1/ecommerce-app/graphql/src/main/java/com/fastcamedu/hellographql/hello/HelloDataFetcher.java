package com.fastcamedu.hellographql.hello;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.Objects;

@DgsComponent
public class HelloDataFetcher {
    @DgsQuery
    public String hello(@InputArgument String name) {
        if (Objects.isNull(name)) {
            name = "Nobody";
        }
        return "Hello, " + name;
    }
}
