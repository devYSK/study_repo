package com.ys.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter
public class ResponseDto<T> {

    private String error;

    private List<T> data;
}
