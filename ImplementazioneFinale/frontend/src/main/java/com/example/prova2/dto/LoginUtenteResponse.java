package com.example.prova2.dto;


public class LoginUtenteResponse {
    private String role;

    private String token;

    private boolean admin;

    public LoginUtenteResponse(){}

    public void setToken(String token) {
        this.token = token;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getAdmin() {
        return admin;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
