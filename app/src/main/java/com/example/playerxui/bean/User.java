package com.example.playerxui.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Created by dang on 2020-12-14.
 * Time will tell.
 *
 * @description
 */
public class User extends LitePalSupport {
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
