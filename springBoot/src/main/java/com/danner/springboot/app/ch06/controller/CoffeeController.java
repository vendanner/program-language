package com.danner.springboot.app.ch06.controller;


import com.danner.springboot.app.ch06.controller.request.NewCoffeeRequest;
import com.danner.springboot.app.ch06.controller.request.NewOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;

/**
 * controller 注解使用
 * @RestController      Contoller + responbody
 * @RequestMapping      请求路径，加在类上，类中所有方法都在此路径下
 * @GetMapping          get 请求，value 是path
 * @PostMapping         post 请求，value 是path
 * @RequestBody         请求体
 */
@Slf4j
@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    // 参数不能包含 name，其实这里本来就不带参数，只是为了与getByName 区分
    @GetMapping(path = "/", params = "!name")
    public String getCoffee() {
        return "coffee";
    }

    @GetMapping("/")
    public String getByName(@RequestParam String name) {
        return name;
    }

    /** 请求和返回都是 json，请求头必须包含
     * {
     *   "Content-Type": "application/json",
     *   "Accept": "application/json;charset=UTF-8"
     * }
     * @param newOrderRequest
     * @return
     */
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestBody NewOrderRequest newOrderRequest) {
        log.info(newOrderRequest.toString());
        return newOrderRequest.getCustomer();
    }

    // 请求方式 get，返回 json格式，路径 /id
    @RequestMapping(path = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getById(@PathVariable Long id) {
        return "getById";
    }


    // Valid 会去校验 NewCoffeeRequest 字段，是否满足条件
    // 若校验失败，返回报错信息（BindingResult）
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String addCoffee(@Valid NewCoffeeRequest newCoffeeRequest,
                            BindingResult result) {
        if (result.hasErrors()) {
            // 这里先简单处理一下，后续讲到异常处理时会改
            log.warn("Binding Errors: {}", result);
            return null;
        }
        return newCoffeeRequest.getName();
    }

    @PostMapping("/file")
    public String addFile(@RequestParam("file")MultipartFile file){
        return file.getName();
    }

    @GetMapping("/exception")
    public String addException() {
        throw new ValidationException("exception msg");
    }
}
