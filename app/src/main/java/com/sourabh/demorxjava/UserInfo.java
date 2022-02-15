package com.sourabh.demorxjava;

public class UserInfo {
    public final int id;
    public final String login;
    public final String type;
    public final String avatar_url;

    public UserInfo(int id, String login, String type, String avatar_url) {
        this.id = id;
        this.login = login;
        this.type = type;
        this.avatar_url = avatar_url;
    }
}