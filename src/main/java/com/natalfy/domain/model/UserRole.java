package com.natalfy.domain.model;

public enum UserRole {
    ADMIN("admin"),
    USER("user"); // O user será o nosso Auxiliar/Recepcionista

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}