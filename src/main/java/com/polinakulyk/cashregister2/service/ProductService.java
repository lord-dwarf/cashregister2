package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.db.repository.ProductRepository;
import com.polinakulyk.cashregister2.exception.CashRegisterEntityNotFoundException;
import com.polinakulyk.cashregister2.service.dto.ProductFilterKind;
import com.polinakulyk.cashregister2.util.Util;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.util.Util.quote;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private static final int FOUND_PRODUCTS_LIMIT = 5;

    private final ProductRepository productRepository = new ProductRepository();

    public List<Product> findAll() {
        var products = productRepository.findAll();

        log.debug("DONE Find products: {}", products.size());
        return products;
    }

    public List<Product> findWithPagination(int page, int rowsPerPage) {
        var products = productRepository.findWithPagination(
                rowsPerPage, (page - 1) * rowsPerPage);

        log.debug("DONE Find products with pagination: {}", products.size());
        return products;
    }

    public int count() {
        var productsTotal = productRepository.count();

        log.debug("DONE Count products: {}", productsTotal);
        return productsTotal;
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

    /**
     * Find the existing product by id, otherwise throw
     * {@link CashRegisterEntityNotFoundException}.
     * <p>
     * Used as a way to retrieve the product entity that must be present. Otherwise the specific
     * exception is thrown, that will result in HTTP 404.
     *
     * @param productId
     * @return
     */
    public Product findExistingById(String productId) {
        var product = productRepository.findById(productId).orElseThrow(() ->
                new CashRegisterEntityNotFoundException(productId));

        log.debug("DONE Find product: '{}'", productId);
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

    public List<Product> findByFilter(ProductFilterKind filterKind, String filterValue) {
        Pattern filterPattern = Pattern.compile(filterValue + ".*", Pattern.CASE_INSENSITIVE);
        Function<Product, String> fun;
        switch (filterKind) {
            case CODE: {
                fun = Product::getCode;
                break;
            }
            case NAME: {
                fun = Product::getName;
                break;
            }
            default:
                throw new UnsupportedOperationException(quote(
                        "Product filter kind not supported", filterKind));
        }
        var getProductFieldFun = fun;
        var filteredProducts = stream(productRepository.findAll().spliterator(), false)
                .filter(p -> filterPattern.matcher(getProductFieldFun.apply(p)).matches())
                .limit(FOUND_PRODUCTS_LIMIT)
                .collect(toList());

        log.debug("DONE Filter products: {}", filteredProducts.size());
        return filteredProducts;
    }
}
