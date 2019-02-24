package com.automonia.core.model.tuple;

/**
 * @作者 温腾
 * @创建时间 2018年01月31日 上午9:22
 */
public class ThreeTuple<A, B, C> extends Tuple {
    public final A first;

    public final B second;

    public final C third;

    public ThreeTuple(A a, B b, C c) {
        this.first = a;
        this.second = b;
        this.third = c;
    }
}
