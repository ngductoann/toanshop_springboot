package com.toan.toanshop.request;

import com.toan.toanshop.model.Category;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {

    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private Category category;
}
