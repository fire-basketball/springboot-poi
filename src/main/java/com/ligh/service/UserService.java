package com.ligh.service;

import com.ligh.domain.User;

import java.util.List;

/**
 * Created by ${ligh} on 2019/1/25 上午10:24
 */
public interface UserService {

    public List<User> queryAllUser();

    public void insertUser(User user);
}
