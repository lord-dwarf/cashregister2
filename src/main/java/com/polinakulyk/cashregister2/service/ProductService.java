package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.Transactional;
import com.polinakulyk.cashregister2.db.dao.ProductDao;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.exception.CashRegisterEntityNotFoundException;
import com.polinakulyk.cashregister2.service.dto.ProductFilterKind;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.util.Util.quote;

import static java.util.stream.Collectors.toList;

/**
 * Product service.
 */
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private static final int FOUND_PRODUCTS_LIMIT = 5;

    private final ProductDao productDao = new ProductDao();

    public List<Product> findWithPagination(int page, int rowsPerPage) {
        var products = productDao.findWithPagination(
                rowsPerPage, (page - 1) * rowsPerPage);

        log.debug("DONE Find products with pagination: {}", products.size());
        return products;
    }

    public int count() {
        var productsTotal = productDao.count();

        log.debug("DONE Count products: {}", productsTotal);
        return productsTotal;
    }

    public Product create(String userId, Product product) {
        log.debug("BEGIN Create product by user: '{}'", userId);

        product = productDao.insert(product);

        log.info("DONE Create product by user: '{}', product: '{}'",
                userId, product.getId());
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
        try (Transactional t = Transactional.beginOrContinueTransaction()) {
            var product = productDao.findById(productId).orElseThrow(() ->
                    new CashRegisterEntityNotFoundException(productId));

            t.commitIfNeeded();
            log.debug("DONE Find product: '{}'", productId);

            return product;
        }
    }

    /**
     * Updates product (not upserts).
     *
     * @param product
     * @return
     */
    public boolean update(Product product) {
        log.debug("BEGIN Update product with id: '{}'", product.getId());

        try (Transactional t = Transactional.beginOrContinueTransaction()) {
            var isUpdated = productDao.update(product);

            if (!isUpdated) {
                log.info("DONE Can't update product with id: {}", product.getId());
                return false;
            }

            t.commitIfNeeded();
            log.info("DONE Update product with id: {}", product.getId());

            return true;
        }
    }

    /**
     * Provides list of products selected by a given filter. The product field for filtering
     * depends on a supplied product filter kind.
     *
     * @param filterKind
     * @param filterValue
     * @return
     */
    public List<Product> findByFilter(ProductFilterKind filterKind, String filterValue) {
        Pattern filterPattern = Pattern.compile(filterValue + ".*", Pattern.CASE_INSENSITIVE);

        Function<Product, String> fun = switch (filterKind) {
            case CODE -> Product::getCode;
            case NAME -> Product::getName;
            default -> throw new UnsupportedOperationException(
                    quote("Product filter kind not supported", filterKind));
        };
        var filteredProducts = productDao.findAll().stream()
                .filter(p -> filterPattern.matcher(fun.apply(p)).matches())
                .limit(FOUND_PRODUCTS_LIMIT)
                .collect(toList());

        log.debug("DONE Filter products: {}", filteredProducts.size());
        return filteredProducts;
    }
}
