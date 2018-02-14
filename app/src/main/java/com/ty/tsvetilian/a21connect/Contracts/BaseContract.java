package com.ty.tsvetilian.a21connect.Contracts;

/**
 * Base Interface for the View and Presenter in MVP
 * @param <T>
 */
public interface BaseContract<T> {
    interface View<T> {

    }
    interface Presenter<T> {
        void attach(T view);
        void detach();
    }
}
