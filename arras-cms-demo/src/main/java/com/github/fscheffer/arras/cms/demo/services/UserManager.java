package com.github.fscheffer.arras.cms.demo.services;

import com.github.fscheffer.arras.cms.services.PermissionManager;

public interface UserManager extends PermissionManager {

    boolean isLoggedIn();

    void login();

    void logout();
}
