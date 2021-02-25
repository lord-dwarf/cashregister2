package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.db.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository = new ProductRepository();

    public List<Product> findAll() {
        var products = productRepository.findAll();

        log.debug("DONE Find products: {}", products.size());
        return products;
    }

    public Product create(String userId, Product product) {
        log.debug("BEGIN Create product by user: '{}'", userId);

        product = productRepository.insert(product);

        log.info("DONE Create product by user: '{}', product: '{}'",
                userId, product.getId());
        return product;
    }

    public Optional<Product> findById(String productId) {
        var product = productRepository.findById(productId);

        if (product.isEmpty()) {
            log.info("DONE Can't find product with id: {}", productId);
            return product;
        }

        log.debug("DONE Find product with id: {}", productId);
        return product;
    }

    public boolean update(Product product) {
        var isUpdated = productRepository.update(product);

        if (!isUpdated) {
            log.info("DONE Can't update product with id: {}", product.getId());
            return false;
        }

        log.debug("DONE Update product with id: {}", product.getId());
        return true;
    }
}
