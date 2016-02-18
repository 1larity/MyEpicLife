package com.digitale.database;

public interface Filter<T,E> {
    public boolean isMatched(T object, E text);
}