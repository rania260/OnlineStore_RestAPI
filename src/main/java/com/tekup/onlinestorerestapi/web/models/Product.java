package com.tekup.onlinestorerestapi.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Product {
    private Long id;
    private String code;
    private String name;
    private Double price;
    private int quantity;
    private String image;
}