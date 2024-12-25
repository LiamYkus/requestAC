package com.example.dao;

import java.util.List;

public interface GenericDAO<T> {
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    T findById(Object id);
    List<T> findAll();
}
