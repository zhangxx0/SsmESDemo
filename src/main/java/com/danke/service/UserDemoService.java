package com.danke.service;

import com.danke.entity.UserDemo;

import java.util.List;

/**
 * demo Service
 */
public interface UserDemoService {

    List queryAll();

    UserDemo queryById(Long id);
}
