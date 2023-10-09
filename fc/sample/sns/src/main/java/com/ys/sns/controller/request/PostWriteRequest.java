package com.ys.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostWriteRequest {
    private String title;
    private String body;
}
