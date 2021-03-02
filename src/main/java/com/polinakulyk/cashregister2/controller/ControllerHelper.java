package com.polinakulyk.cashregister2.controller;

/**
 * Utility class for controller layer.
 */
public final class ControllerHelper {

    private ControllerHelper() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    /**
     * Calculate number of pagination pages based on a number of rows.
     *
     * @param rowsTotal
     * @param rowsPerPage
     * @return number of pages
     */
    public static int calcPaginationPagesTotal(int rowsTotal, int rowsPerPage) {
        var pagesTotal = rowsTotal / rowsPerPage;
        if (rowsTotal % rowsPerPage != 0) {
            ++pagesTotal;
        }
        return pagesTotal;
    }
}
