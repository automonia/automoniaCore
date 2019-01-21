package com.automonia.core.base.tuple;

/**
 * @作者 温腾
 * @创建时间 2018年01月31日 上午9:21
 */
public class Tuple {

    public static <A, B> TwoTuple<A, B> tuple(A a, B b) {
        return new TwoTuple(a, b);
    }

    public static <A, B, C> ThreeTuple<A, B, C> tuple(A a, B b, C c) {
        return new ThreeTuple<>(a, b, c);
    }
}
