package com.git.blog.config;

import java.util.concurrent.ThreadFactory;

/**
 *
 * @author Administrator
 */
public class LocalThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
