package com.automonia.core.model.tuple;

/**
 * @作者 温腾
 * @创建时间 2018年01月31日 上午9:21
 */
public class TwoTuple<A, B> extends Tuple {

    public final A first;

    public final B second;

    public TwoTuple(A a, B b) {
        this.first = a;
        this.second = b;
    }


    @Override
    public String toString() {
        return "first: " + first + "; second: " + second;
    }
}
