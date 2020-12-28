package com.feng.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ShiroUsernamePasswordToken extends UsernamePasswordToken {

    private String token;

    public ShiroUsernamePasswordToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
