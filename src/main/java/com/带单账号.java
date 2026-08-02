package com;

public class 带单账号 {

    String id;
    String name;
    String token;
    String cookie;

    public 带单账号(String id, String name, String csrftoken, String cookie) {
        this.id = id;
        this.name = name;
        this.token = csrftoken;
        this.cookie = cookie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
