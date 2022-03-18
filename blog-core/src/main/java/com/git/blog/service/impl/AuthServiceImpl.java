package com.git.blog.service.impl;

import com.git.blog.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author authorZhao
 * @since 2022-03-06
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Override
    public boolean checkAuth(Long uid, String menuName) {
        return true;
    }
}
