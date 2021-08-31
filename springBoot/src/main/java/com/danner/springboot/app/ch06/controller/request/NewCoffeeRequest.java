package com.danner.springboot.app.ch06.controller.request;


import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewCoffeeRequest {

    @NotEmpty
    private String name;

    @NotNull
    private String price;
}
