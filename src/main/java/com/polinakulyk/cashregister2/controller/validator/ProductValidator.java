package com.polinakulyk.cashregister2.controller.validator;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.exception.CashRegisterValidationException;
import com.polinakulyk.cashregister2.service.dto.ProductFilterKind;

import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;

import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalAmountNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalGreaterThanZero;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalLessThan;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalMoneyNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validBigDecimalNonNegative;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNotNull;
import static com.polinakulyk.cashregister2.controller.validator.ValidatorHelper.validStringNullable;
import static com.polinakulyk.cashregister2.util.Util.bigDecimalMoney;
import static com.polinakulyk.cashregister2.util.Util.quote;

import static java.util.Arrays.asList;

public class ProductValidator {

    private ProductValidator() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    private static final String PRODUCT_ID_PARAM = "id";
    private static final String PRODUCT_AMOUNT_UNIT_PARAM = "amountUnit";
    private static final String PRODUCT_CODE_PARAM = "code";
    private static final String PRODUCT_NAME_PARAM = "name";
    private static final String PRODUCT_CATEGORY_PARAM = "category";
    private static final String PRODUCT_PRICE_PARAM = "price";
    private static final String PRODUCT_AMOUNT_AVAILABLE_PARAM = "amountAvailable";
    private static final String PRODUCT_DETAILS_PARAM = "details";

    private static final BigDecimal PRODUCT_AMOUNT_UPPER_BOUND = new BigDecimal(10_000);
    private static final BigDecimal PRODUCT_PRICE_UPPER_BOUND = bigDecimalMoney("100000");

    public static Product validProductFromRequest(HttpServletRequest request) {
        Product product = validProductFromRequestWithoutId(request);
        return product.setId(validStringNotNull(request, PRODUCT_ID_PARAM));
    }

    public static Product validProductFromRequestWithoutId(HttpServletRequest request) {
        Product product = new Product();

        var code = validStringNotNull(request, PRODUCT_CODE_PARAM);
        var name = validStringNotNull(request, PRODUCT_NAME_PARAM);
        var category = validStringNotNull(request, PRODUCT_CATEGORY_PARAM);
        var amountUnit = validProductAmountUnitNotNull(request, PRODUCT_AMOUNT_UNIT_PARAM);
        var price = validBigDecimalMoneyNotNull(request, PRODUCT_PRICE_PARAM);

        // validate price within bounds
        validBigDecimalGreaterThanZero(price, PRODUCT_PRICE_PARAM);
        validBigDecimalLessThan(price, PRODUCT_PRICE_UPPER_BOUND, PRODUCT_PRICE_PARAM);

        var amountAvailable =
                validBigDecimalAmountNotNull(request, PRODUCT_AMOUNT_AVAILABLE_PARAM, amountUnit);

        // validate amount available within bounds
        validBigDecimalNonNegative(amountAvailable, PRODUCT_AMOUNT_AVAILABLE_PARAM);
        validBigDecimalLessThan(
                amountAvailable, PRODUCT_AMOUNT_UPPER_BOUND, PRODUCT_AMOUNT_AVAILABLE_PARAM);

        var details = validStringNullable(request, PRODUCT_DETAILS_PARAM)
                .orElse(null);

        return product
                .setCode(code)
                .setName(name)
                .setCategory(category)
                .setAmountUnit(amountUnit)
                .setPrice(price)
                .setAmountAvailable(amountAvailable)
                .setDetails(details);
    }

    public static ProductAmountUnit validProductAmountUnitNotNull(
            HttpServletRequest request, String paramName) {
        return ProductAmountUnit.fromString(validStringNotNull(request, paramName))
                .orElseThrow(() -> new CashRegisterValidationException(
                        quote("Product amount unit must be one of ",
                                asList(ProductAmountUnit.values()))
                ));
    }

    public static ProductFilterKind validProductFilterKindNotNull(
            HttpServletRequest request, String paramName) {
        return ProductFilterKind
                .fromString(validStringNotNull(request, paramName))
                .orElseThrow(() -> new CashRegisterValidationException(
                        "Product filter kind is not valid"));
    }
}
