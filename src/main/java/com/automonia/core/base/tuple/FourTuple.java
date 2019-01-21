package com.automonia.core.base.tuple;

/**
 * @作者 温腾
 * @创建时间 2018年01月31日 上午9:22
 */
public class FourTuple<A, B, C, D> extends Tuple {
    private final A first;

    private final B second;

    private final C third;

    private final D fourth;

    public FourTuple(A a, B b, C c, D d) {
        this.first = a;
        this.second = b;
        this.third = c;
        this.fourth = d;
    }
}
