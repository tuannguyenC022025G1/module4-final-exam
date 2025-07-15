package com.finalexam.controller;

import com.finalexam.model.Product;
import com.finalexam.model.ProductType;
import com.finalexam.repository.ProductTypeRepository;
import com.finalexam.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controller for handling product-related requests.
 */
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductTypeRepository productTypeRepository;

    @GetMapping("/products")
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") Integer productTypeId,
            @RequestParam(defaultValue = "") Double minPrice,
            Model model) {
        int pageSize = 5;
        List<Product> allProducts = productService.searchProducts(
                name.isEmpty() ? null : name,
                productTypeId == 0 ? null : productTypeId,
                minPrice == 0 ? null : minPrice);
        int totalItems = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int start = page * pageSize;
        int end = Math.min(start + pageSize, totalItems);
        List<Product> paginatedProducts = allProducts.subList(start, end);

        model.addAttribute("products", paginatedProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("name", name);
        model.addAttribute("productTypeId", productTypeId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("productTypes", productTypeRepository.findAll());
        return "product/list";
    }

    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("productTypes", productTypeRepository.findAll());
        return "product/add";
    }

    @PostMapping("/products/add")
    public String addProduct(Product product, Model model) {
        if (product.getName() == null || product.getName().length() < 5 || product.getName().length() > 50) {
            model.addAttribute("error", "Product name must be between 5 and 50 characters.");
            model.addAttribute("product", product);
            model.addAttribute("productTypes", productTypeRepository.findAll());
            return "product/add";
        }
        if (product.getPrice() < 100000) {
            model.addAttribute("error", "Starting price must be at least 100,000 VND.");
            model.addAttribute("product", product);
            model.addAttribute("productTypes", productTypeRepository.findAll());
            return "product/add";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}