package org.github.fscheffer.arras.cms.demo.services;

import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

public class SimpleUserManager implements UserManager {

    private static String USER_LOGGED_IN = "user-logged-in";

    private Request       request;

    public SimpleUserManager(Request request) {
        this.request = request;
    }

    @Override
    public boolean hasPermissionToEdit() {
        return isLoggedIn();
    }

    @Override
    public boolean isLoggedIn() {
        Object value = session().getAttribute(USER_LOGGED_IN);
        return value != null && Boolean.class.cast(value) == true;
    }

    @Override
    public void login() {
        session().setAttribute(USER_LOGGED_IN, true);
    }

    @Override
    public void logout() {
        session().setAttribute(USER_LOGGED_IN, null);
    }

    private Session session() {
        return this.request.getSession(true);
    }
}
