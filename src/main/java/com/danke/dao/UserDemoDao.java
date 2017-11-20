package com.danke.dao;

import com.danke.entity.UserDemo;

import java.util.List;

/**
 * demo DAO
 *
 * @date 2017年11月20日11:19:02
 */
public interface UserDemoDao {
    List queryAll();
    UserDemo queryById(Long id);
}
