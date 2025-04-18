package com.git.blog.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author authorZhao
 * @since 2022-08-30
 */
@Component
public class ThreadPoolConfig {

    @Bean("commonPoolExecutor")
    public ThreadPoolExecutor commonPoolExecutor() {
        return new ThreadPoolExecutor(2, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1000), new ThreadNameFactory("通用线程池"),
                //超过最大队列在该线程中处理
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Slf4j
    public static class ThreadNameFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final boolean virtual;

        public ThreadNameFactory() {
            group = Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
            this.virtual = false;
        }

        public ThreadNameFactory(String namePrefix) {
            this(namePrefix,null,false);
        }

        public ThreadNameFactory(String namePrefix,boolean virtual) {
            this(namePrefix,null,virtual);
        }

        public ThreadNameFactory(String namePrefix,ThreadGroup group,boolean virtual) {
            if(group!=null){
                this.group = group;
            }else {
                this.group = Thread.currentThread().getThreadGroup();
            }
            this.virtual = virtual;
            this.namePrefix = namePrefix+"-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }



        @Override
        public Thread newThread(Runnable runnable) {
            Thread t;
            if (virtual) {
                t = Thread.ofVirtual().name(namePrefix + threadNumber.getAndIncrement()).unstarted(runnable);
            } else {
                t = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
            }
            t.setUncaughtExceptionHandler((t1, e) -> log.error("线程异常name={},id={}", t1.getName(), t1.getId(), e));
            return t;
        }
    }
}
