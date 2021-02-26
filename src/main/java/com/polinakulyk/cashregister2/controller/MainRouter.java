package com.polinakulyk.cashregister2.controller;

import com.polinakulyk.cashregister2.controller.api.HttpRoute;
import com.polinakulyk.cashregister2.controller.command.AddProductCommand;
import com.polinakulyk.cashregister2.controller.command.AddReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.AddReceiptItemCommand;
import com.polinakulyk.cashregister2.controller.command.BeforeAddReceiptItemCommand;
import com.polinakulyk.cashregister2.controller.command.CancelMyReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.CancelReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.CompleteMyReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.GetAuthLogoutCommand;
import com.polinakulyk.cashregister2.controller.command.GetProductCommand;
import com.polinakulyk.cashregister2.controller.command.GetReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.GetReportsXCommand;
import com.polinakulyk.cashregister2.controller.command.GetReportsZCommand;
import com.polinakulyk.cashregister2.controller.command.ListMyReceiptsCommand;
import com.polinakulyk.cashregister2.controller.command.ListProductsCommand;
import com.polinakulyk.cashregister2.controller.command.ListReceiptsCommand;
import com.polinakulyk.cashregister2.controller.command.LoginCommand;
import com.polinakulyk.cashregister2.controller.command.SearchReceiptItemCommand;
import com.polinakulyk.cashregister2.controller.command.UpdateProductCommand;
import java.util.Set;

import static com.polinakulyk.cashregister2.controller.api.HttpMethod.*;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGIN;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.AUTH_LOGOUT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_CLIENT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.ERROR_SERVER;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.INDEX;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_ADD;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_ADDITEM;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_CANCEL;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_COMPLETE;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_EDIT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_ADDITEM_SEARCH;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.MYRECEIPTS_VIEW;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_ADD;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_EDIT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.PRODUCTS_VIEW;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_CANCEL;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_EDIT;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.api.HttpRoute.RECEIPTS_VIEW;
import static com.polinakulyk.cashregister2.security.dto.UserRole.GUEST;
import static com.polinakulyk.cashregister2.security.dto.UserRole.MERCH;
import static com.polinakulyk.cashregister2.security.dto.UserRole.SR_TELLER;
import static com.polinakulyk.cashregister2.security.dto.UserRole.any;
import static com.polinakulyk.cashregister2.security.dto.UserRole.authenticated;
import static com.polinakulyk.cashregister2.security.dto.UserRole.tellers;

public class MainRouter extends Router {

    public static class Singleton {
        public static final Router INSTANCE = new MainRouter();
    }

    public MainRouter() {
        // Home
        addCommand(GET, INDEX, "index.jsp", any());
        // Products and Products -> Product
        addCommandThenForward(GET, PRODUCTS_LIST, new ListProductsCommand(), "products/list.jsp", authenticated());
        addCommand(GET, PRODUCTS_ADD, "products/add.jsp", Set.of(MERCH));
        addCommand(POST, PRODUCTS_ADD, new AddProductCommand(), Set.of(MERCH));
        addCommandThenForward(GET, PRODUCTS_VIEW, new GetProductCommand(), "products/view.jsp", authenticated());
        addCommandThenForward(GET, PRODUCTS_EDIT, new GetProductCommand(), "products/edit.jsp", Set.of(MERCH));
        addCommand(POST, PRODUCTS_EDIT, new UpdateProductCommand(), Set.of(MERCH));
        // Receipts and Receipts -> Receipt
        addCommandThenForward(GET, RECEIPTS_LIST, new ListReceiptsCommand(), "receipts/list.jsp", Set.of(SR_TELLER));
        addCommandThenForward(GET, RECEIPTS_VIEW, new GetReceiptCommand(), "receipts/view.jsp", Set.of(SR_TELLER));
        addCommandThenForward(GET, RECEIPTS_EDIT, new GetReceiptCommand(), "receipts/edit.jsp", Set.of(SR_TELLER));
        addCommand(POST, RECEIPTS_CANCEL, new CancelReceiptCommand(), Set.of(SR_TELLER));
        // My Receipts and My Receipts -> Receipt
        addCommandThenForward(GET, MYRECEIPTS_LIST, new ListMyReceiptsCommand(), "myreceipts/list.jsp", tellers());
        addCommandThenForward(GET, MYRECEIPTS_ADD, new AddReceiptCommand(), "myreceipts/edit.jsp", tellers());
        addCommandThenForward(GET, MYRECEIPTS_VIEW, new GetReceiptCommand(), "myreceipts/view.jsp", tellers());
        addCommandThenForward(GET, MYRECEIPTS_EDIT, new GetReceiptCommand(), "myreceipts/edit.jsp", tellers());
        addCommand(POST, MYRECEIPTS_CANCEL, new CancelMyReceiptCommand(), Set.of(SR_TELLER));
        addCommand(POST, MYRECEIPTS_COMPLETE, new CompleteMyReceiptCommand(), tellers());

        // My Receipts -> Receipt -> Receipt Item
        addCommandThenForward(GET, MYRECEIPTS_ADDITEM, new BeforeAddReceiptItemCommand(), "myreceipts/additem.jsp", tellers());
        addCommandThenForward(POST, MYRECEIPTS_ADDITEM_SEARCH, new SearchReceiptItemCommand(), "myreceipts/additem.jsp", tellers());
        addCommand(POST, MYRECEIPTS_ADDITEM, new AddReceiptItemCommand(), tellers());
        // Reports
        addCommand(GET, HttpRoute.REPORTS_LIST, "reports/list.jsp", Set.of(SR_TELLER));
        addCommand(GET, HttpRoute.REPORTS_X, new GetReportsXCommand(), Set.of(SR_TELLER));
        addCommand(GET, HttpRoute.REPORTS_Z, new GetReportsZCommand(), Set.of(SR_TELLER));
        // Auth
        addCommand(GET, AUTH_LOGIN, "auth/login.jsp", Set.of(GUEST));
        addCommand(POST, AUTH_LOGIN, new LoginCommand(), any());
        addCommand(GET, AUTH_LOGOUT, new GetAuthLogoutCommand(), authenticated());
        // Errors
        addCommand(GET, ERROR_CLIENT, "error/client.jsp", any());
        addCommand(GET, ERROR_AUTH, "error/auth.jsp", any());
        addCommand(GET, ERROR_NOTFOUND, "error/notfound.jsp", any());
        addCommand(GET, ERROR_SERVER, "error/server.jsp", any());
    }
}
