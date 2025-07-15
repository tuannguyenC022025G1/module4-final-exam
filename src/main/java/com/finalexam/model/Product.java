package com.finalexam.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model class representing a product.
 */
@Getter
@Setter
public class Product {
    private int id;
    private String name;
    private double price;
    private String status;
    private ProductType productType;
}