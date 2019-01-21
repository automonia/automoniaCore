package com.automonia.core.utils;

//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志工具类
 *
 * @author 作者 温腾
 * @创建时间 2017年5月9日 上午12:26:08
 */
public enum LogUtils {

    singletion;

    /*
    存放已创建过的Logger对象，key值使用类的simpleName
     */
    private Map<String, Logger> warehouse = new HashMap<String, Logger>();

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * debug级别输出日志信息
     */
    public void debug(String message) {
        getCallerLogger().debug(message);
    }

    /**
     * error级别输出日志信息
     */
    public void error(String message) {
        getCallerLogger().error(message);
    }

    /**
     * info级别输出日志信息
     */
    public void info(String message) {
        getCallerLogger().info(message);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 获取调用者所在对象的Logger对象
     *
     * @return Logger对象
     */
    private Logger getCallerLogger() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String classSimpleName = stackTraceElements[3].getClassName();

        Logger logger = warehouse.get(classSimpleName);
        if (logger == null) {
            logger = LoggerFactory.getLogger(stackTraceElements[3].getClassName());

            // 记录存放
            warehouse.put(classSimpleName, logger);

        }
        return logger;
    }

}
