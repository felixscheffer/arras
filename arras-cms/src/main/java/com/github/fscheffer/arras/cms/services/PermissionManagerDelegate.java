package com.github.fscheffer.arras.cms.services;

public class PermissionManagerDelegate implements PermissionManager {

    private final PermissionManager manager;

    public PermissionManagerDelegate(PermissionManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean hasPermissionToEdit() {
        return this.manager.hasPermissionToEdit();
    }
}