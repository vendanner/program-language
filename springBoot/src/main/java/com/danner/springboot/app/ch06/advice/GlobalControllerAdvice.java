package com.danner.springboot.app.ch06.advice;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Advice 后缀是 spring boot 提供的切面
 *  RestControllerAdvice:  RestController 切面
 */
@RestControllerAdvice
public class GlobalControllerAdvice {

    /**
     * 专门捕获 ValidationException
     * @param ex
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler(ValidationException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return map;
    }
}
