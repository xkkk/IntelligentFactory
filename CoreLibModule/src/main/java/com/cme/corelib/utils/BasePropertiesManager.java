package com.cme.corelib.utils;

import java.util.Properties;

/**
 * Created by klx on 2017/12/14.
 * 配置文件操作类
 */

public abstract class BasePropertiesManager {
    /**
     * 读取配置
     *
     * @param propertyName
     */
    public String getProperty(String propertyName) {
        Properties p = getProperties();
        if (p != null) {
            return p.getProperty(propertyName);
        }
        return "";
    }

    /**
     * 设置配置
     *
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        Properties p = getProperties();
        if (p != null) {
            p.setProperty(key, value);
        }
    }

    protected abstract Properties getProperties();
}
