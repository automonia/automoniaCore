package com.automonia.core.tools;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @作者 温腾
 * @创建时间 2019年01月26日 17:18
 */
public enum CollectionUtils {

    singleton;


    /**
     * 求两个数组的交集
     *
     * @param array1 数组1
     * @param array2 数组2
     * @return 数组交集
     */
    public <T extends Comparator<T>> List<T> intersection(List<T> array1, List<T> array2) {
        if (array1 == null || array2 == null) {
            return null;
        }
        return array1.stream().filter(array2::contains).collect(Collectors.toList());
    }

    /**
     * 求两个Field数组的并集
     * 这里为Field对象单独开发函数，是因为Field对象并不是Object的子类,并不能用范型T表示
     *
     * @param array1 数组1
     * @param array2 数组2
     * @return 数组并集
     */
    public Field[] union(Field[] array1, Field[] array2) {
        if (array1 == null && array2 == null)
            return null;

        /**
         * 计算出并集数组的长度
         */
        int count = (array1 == null ? 0 : array1.length) + (array2 == null ? 0 : array2.length);

        Field[] targets = new Field[count];
        int index = 0;

        if (array1 != null) {
            for (Field item : array1) {
                targets[index++] = item;
            }
        }
        if (array2 != null) {
            for (Field item : array2) {
                targets[index++] = item;
            }
        }
        return targets;
    }

    /**
     * 求两个数组的并集,
     *
     * @param array1 数组1
     * @param array2 数组2
     * @return 数组并集
     */
    public <T> List<T> union(List<T> array1, List<T> array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        List<T> targets = array1 != null ? array1 : array2;

        if (array1 != null) {
            targets.addAll(array2.stream().collect(Collectors.toList()));
        }
        return targets;
    }
}
