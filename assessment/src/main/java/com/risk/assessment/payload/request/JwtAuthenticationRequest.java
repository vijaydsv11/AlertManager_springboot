package com.risk.assessment.payload.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    @NotBlank
    private String username;
    
    @NotBlank
    private CharSequence password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CharSequence getPassword() {
        return this.password;
    }

    public void setPassword(CharSequence password) {
        this.password = password;
    }
}
