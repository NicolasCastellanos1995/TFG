package com.example.demo.dto;

public class LoginResponse {

    private String token;
    private String username;
    private String role;
    private String redirectUrl;

    public LoginResponse() {
    }

    public LoginResponse(String token, String username, String role, String redirectUrl) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.redirectUrl = redirectUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}