package com.ligh.serviceImpl;

import com.ligh.domain.User;
import com.ligh.domain.UserExample;
import com.ligh.mapper.UserMapper;
import com.ligh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ${ligh} on 2019/1/25 上午10:26
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryAllUser() {
        UserExample example = new UserExample();
        example.createCriteria();
        List<User> users = userMapper.selectByExample(example);
        return users;
    }

    @Override
    public void insertUser(User user) {
        userMapper.insertSelective(user);
    }
}
