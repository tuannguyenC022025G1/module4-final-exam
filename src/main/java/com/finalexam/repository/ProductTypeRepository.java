package com.finalexam.repository;

import com.finalexam.model.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for ProductType entity using JDBC.
 */
@Repository
public class ProductTypeRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductType> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM product_type",
                (rs, rowNum) -> {
                    ProductType pt = new ProductType();
                    pt.setId(rs.getInt("id"));
                    pt.setName(rs.getString("name"));
                    return pt;
                });
    }
}