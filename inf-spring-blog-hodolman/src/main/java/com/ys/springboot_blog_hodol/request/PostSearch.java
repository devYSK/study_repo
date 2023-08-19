package com.ys.springboot_blog_hodol.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.Math.*;
import static java.lang.Math.max;

/**
 * @author : ysk
 */
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class PostSearch {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long getOffSet() {
        return (long)(max(1, page) - 1) * min(size, 2000);
    }

}
