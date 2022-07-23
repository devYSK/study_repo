package com.ys.springmvc.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : ysk
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class HelloData {

    private String username;

    private Integer age;
}
