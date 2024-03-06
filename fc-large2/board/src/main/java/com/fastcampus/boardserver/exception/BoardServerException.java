package com.fastcampus.boardserver.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BoardServerException extends RuntimeException {
  HttpStatus code;
  String msg;
}
