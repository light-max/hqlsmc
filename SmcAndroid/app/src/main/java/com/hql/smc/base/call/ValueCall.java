package com.hql.smc.base.call;

public interface ValueCall<T, E> {
    void call(T value, E e);
}
