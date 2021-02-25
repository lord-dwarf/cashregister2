package com.polinakulyk.cashregister2.view;

import javax.servlet.http.HttpServletRequest;

public class RequestView {

    private HttpServletRequest request;

    public void init(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
