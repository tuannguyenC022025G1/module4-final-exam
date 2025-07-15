package com.finalexam.service;

import com.finalexam.model.Product;
import com.finalexam.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing product-related operations.
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> searchProducts(String name, Integer productTypeId, Double minPrice) {
        return productRepository.search(name, productTypeId, minPrice);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(int id) {
        productRepository.delete(id);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}