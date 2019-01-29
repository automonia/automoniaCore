package com.automonia.dialect.delegate;

/**
 * @作者 温腾
 * @创建时间 2019年01月27日 11:15
 */
public interface EntityClassInfoFactoryDelegate {

    /**
     * 返回实体类所在的目录
     * 只能是一个目录
     */
    String getEntityPath();
}
