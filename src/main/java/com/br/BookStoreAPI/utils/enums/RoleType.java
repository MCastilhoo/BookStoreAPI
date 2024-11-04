package com.br.BookStoreAPI.utils.enums;

public enum RoleType {
    ADMIN(1L),
    USER(2L);

    private final long roleId;

    RoleType(long roleId) {
        this.roleId = roleId;
    }

    public long getRoleId() {
        return roleId;
    }
}
