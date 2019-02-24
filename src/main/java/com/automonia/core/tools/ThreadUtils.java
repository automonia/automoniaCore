package com.automonia.core.tools;


import com.automonia.core.annotation.WTThread;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 以后使用线程池管理线程
 *
 * @作者 温腾
 * @创建时间 2018年05月15日 下午9:23
 */
public enum ThreadUtils {

    singleton;

    /*
    线程池，统一管理
     */
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    public void async(WTThread thread) {

        // 创建线程接口对象
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    thread.action();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // 放到线程池种执行线程
        threadPoolExecutor.execute(runnable);
    }
}
