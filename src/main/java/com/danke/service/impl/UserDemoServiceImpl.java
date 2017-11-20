package com.danke.service.impl;

import com.danke.dao.UserDemoDao;
import com.danke.entity.UserDemo;
import com.danke.service.UserDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * demo ServiceImplment
 */
@Service
public class UserDemoServiceImpl implements UserDemoService {

    @Autowired
    private UserDemoDao userDemoDao;

    public List queryAll() {
        return userDemoDao.queryAll();
    }

    public UserDemo queryById(Long id) {
        return null;
    }
}
