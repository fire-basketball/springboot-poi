package com.ligh.domain;

import java.util.Date;

public class User {
    private Long id;

    private String name;

    private String username;

    private Date createTime;

    public User() {
    }

    public User(Long id, String name, String username, Date createTime) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}