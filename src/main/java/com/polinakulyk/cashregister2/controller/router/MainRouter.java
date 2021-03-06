package com.polinakulyk.cashregister2.controller.router;

import com.polinakulyk.cashregister2.controller.command.AddProductCommand;
import com.polinakulyk.cashregister2.controller.command.AddReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.AddReceiptItemCommand;
import com.polinakulyk.cashregister2.controller.command.BeforeAddReceiptItemCommand;
import com.polinakulyk.cashregister2.controller.command.CancelMyReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.CancelReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.ChangeLangCommand;
import com.polinakulyk.cashregister2.controller.command.CompleteMyReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.GetAuthLogoutCommand;
import com.polinakulyk.cashregister2.controller.command.GetErrorMessageCommand;
import com.polinakulyk.cashregister2.controller.command.GetProductCommand;
import com.polinakulyk.cashregister2.controller.command.GetReceiptCommand;
import com.polinakulyk.cashregister2.controller.command.GetReportsCommand;
import com.polinakulyk.cashregister2.controller.command.ListMyReceiptsCommand;
import com.polinakulyk.cashregister2.controller.command.ListProductsCommand;
import com.polinakulyk.cashregister2.controller.command.ListReceiptsCommand;
import com.polinakulyk.cashregister2.controller.command.LoginCommand;
import com.polinakulyk.cashregister2.controller.command.SearchReceiptItemCommand;
import com.polinakulyk.cashregister2.controller.command.UpdateProductCommand;
import com.polinakulyk.cashregister2.service.dto.ReportKind;

import java.util.Set;

import static com.polinakulyk.cashregister2.controller.dto.HttpMethod.GET;
import static com.polinakulyk.cashregister2.controller.dto.HttpMethod.POST;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.AUTH_LANG;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.AUTH_LOGIN;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.AUTH_LOGOUT;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_AUTH;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_CLIENT;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_NOTFOUND;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.ERROR_SERVER;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.INDEX;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_ADD;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_ADDITEM;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_ADDITEM_SEARCH;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_CANCEL;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_COMPLETE;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_EDIT;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.MYRECEIPTS_VIEW;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_ADD;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_EDIT;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_LIST;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.PRODUCTS_VIEW;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.RECEIPTS_CANCEL;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.RECEIPTS_EDIT;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.RECEIPTS_LIST;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.RECEIPTS_VIEW;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.REPORTS_LIST;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.REPORTS_X;
import static com.polinakulyk.cashregister2.controller.dto.HttpRoute.REPORTS_Z;
import static com.polinakulyk.cashregister2.db.dto.UserRole.GUEST;
import static com.polinakulyk.cashregister2.db.dto.UserRole.MERCH;
import static com.polinakulyk.cashregister2.db.dto.UserRole.SR_TELLER;
import static com.polinakulyk.cashregister2.db.dto.UserRole.any;
import static com.polinakulyk.cashregister2.db.dto.UserRole.authenticated;
import static com.polinakulyk.cashregister2.db.dto.UserRole.tellers;

/**
 * The single source of routes and authorization access control in the application.
 * For the structure of mappings, see {@link Router}.
 */
public final class MainRouter extends Router {

    public static class Singleton {
        public static final Router INSTANCE = new MainRouter();
    }

    private MainRouter() {
        // Home
        addForwardToJsp(GET, INDEX, "index.jsp", any());
        // Products and Products -> Product
        addCommandThenForwardToJsp(GET, PRODUCTS_LIST, new ListProductsCommand(), "products/list.jsp", authenticated());
        addForwardToJsp(GET, PRODUCTS_ADD, "products/add.jsp", Set.of(MERCH));
        addCommand(POST, PRODUCTS_ADD, new AddProductCommand(), Set.of(MERCH));
        addCommandThenForwardToJsp(GET, PRODUCTS_VIEW, new GetProductCommand(), "products/view.jsp", authenticated());
        addCommandThenForwardToJsp(GET, PRODUCTS_EDIT, new GetProductCommand(), "products/edit.jsp", Set.of(MERCH));
        addCommand(POST, PRODUCTS_EDIT, new UpdateProductCommand(), Set.of(MERCH));
        // Receipts and Receipts -> Receipt
        addCommandThenForwardToJsp(GET, RECEIPTS_LIST, new ListReceiptsCommand(), "receipts/list.jsp", Set.of(SR_TELLER));
        addCommandThenForwardToJsp(GET, RECEIPTS_VIEW, new GetReceiptCommand(), "receipts/view.jsp", Set.of(SR_TELLER));
        addCommandThenForwardToJsp(GET, RECEIPTS_EDIT, new GetReceiptCommand(), "receipts/edit.jsp", Set.of(SR_TELLER));
        addCommand(POST, RECEIPTS_CANCEL, new CancelReceiptCommand(), Set.of(SR_TELLER));
        // My Receipts and My Receipts -> Receipt
        addCommandThenForwardToJsp(GET, MYRECEIPTS_LIST, new ListMyReceiptsCommand(), "myreceipts/list.jsp", tellers());
        addCommandThenForwardToJsp(GET, MYRECEIPTS_ADD, new AddReceiptCommand(), "myreceipts/edit.jsp", tellers());
        addCommandThenForwardToJsp(GET, MYRECEIPTS_VIEW, new GetReceiptCommand(), "myreceipts/view.jsp", tellers());
        addCommandThenForwardToJsp(GET, MYRECEIPTS_EDIT, new GetReceiptCommand(), "myreceipts/edit.jsp", tellers());
        addCommand(POST, MYRECEIPTS_CANCEL, new CancelMyReceiptCommand(), Set.of(SR_TELLER));
        addCommand(POST, MYRECEIPTS_COMPLETE, new CompleteMyReceiptCommand(), tellers());

        // My Receipts -> Receipt -> Receipt Item
        addCommandThenForwardToJsp(GET, MYRECEIPTS_ADDITEM, new BeforeAddReceiptItemCommand(), "myreceipts/additem.jsp", tellers());
        addCommandThenForwardToJsp(POST, MYRECEIPTS_ADDITEM_SEARCH, new SearchReceiptItemCommand(), "myreceipts/additem.jsp", tellers());
        addCommand(POST, MYRECEIPTS_ADDITEM, new AddReceiptItemCommand(), tellers());
        // Reports
        addForwardToJsp(GET, REPORTS_LIST, "reports/list.jsp", Set.of(SR_TELLER));
        addCommand(GET, REPORTS_X, new GetReportsCommand(ReportKind.X), Set.of(SR_TELLER));
        addCommand(GET, REPORTS_Z, new GetReportsCommand(ReportKind.Z), Set.of(SR_TELLER));
        // Auth
        addForwardToJsp(GET, AUTH_LOGIN, "auth/login.jsp", Set.of(GUEST));
        addCommand(POST, AUTH_LOGIN, new LoginCommand(), any());
        addCommand(GET, AUTH_LOGOUT, new GetAuthLogoutCommand(), authenticated());
        // Localization
        addCommand(POST, AUTH_LANG, new ChangeLangCommand(), any());
        // Errors
        addCommandThenForwardToJsp(
                GET, ERROR_CLIENT, new GetErrorMessageCommand(), "error/client.jsp", any());
        addForwardToJsp(GET, ERROR_AUTH, "error/auth.jsp", any());
        addForwardToJsp(GET, ERROR_NOTFOUND, "error/notfound.jsp", any());
        addForwardToJsp(GET, ERROR_SERVER, "error/server.jsp", any());
    }
}
