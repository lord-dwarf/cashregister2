package com.polinakulyk.cashregister2.controller;

public final class ControllerHelper {

    private ControllerHelper() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static int calcPaginationPagesTotal(int rowsTotal, int rowsPerPage) {
        var pagesTotal = rowsTotal / rowsPerPage;
        if (rowsTotal % rowsPerPage != 0) {
            ++pagesTotal;
        }
        return pagesTotal;
    }
}
