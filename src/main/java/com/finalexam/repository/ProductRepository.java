package com.finalexam.repository;

import com.finalexam.model.Product;
import com.finalexam.model.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for Product entity using JDBC.
 */
@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT p.id, p.name, p.price, p.status, pt.id as type_id, pt.name as type_name " +
                        "FROM product p LEFT JOIN product_type pt ON p.product_type_id = pt.id",
                new ProductRowMapper());
    }

    public List<Product> search(String name, Integer productTypeId, Double minPrice) {
        String sql = "SELECT p.id, p.name, p.price, p.status, pt.id as type_id, pt.name as type_name " +
                "FROM product p LEFT JOIN product_type pt ON p.product_type_id = pt.id " +
                "WHERE (? IS NULL OR p.name LIKE CONCAT('%', ?, '%')) " +
                "AND (? IS NULL OR p.product_type_id = ?) " +
                "AND (? IS NULL OR p.price >= ?)";

        return jdbcTemplate.query(sql,
                new Object[] {
                        name == null ? null : name,
                        name == null ? null : name,
                        productTypeId == null ? null : productTypeId,
                        productTypeId == null ? null : productTypeId,
                        minPrice == null ? null : minPrice,
                        minPrice == null ? null : minPrice
                },
                new ProductRowMapper());
    }

    public void save(Product product) {
        jdbcTemplate.update("INSERT INTO product (name, price, status, product_type_id) VALUES (?, ?, ?, ?)",
                product.getName(), product.getPrice(), product.getStatus(), product.getProductType().getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM product WHERE id = ?", id);
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setStatus(rs.getString("status"));
            ProductType pt = new ProductType();
            pt.setId(rs.getInt("type_id"));
            pt.setName(rs.getString("type_name"));
            product.setProductType(pt);
            return product;
        }
    }
}