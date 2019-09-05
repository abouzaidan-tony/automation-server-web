package com.tony.automationserverweb.dao;

import java.util.List;

public interface IRepository<T, ID> {

    T insert(T object);

    T update(T object);

    T findOneById(ID id);

    List<T> getAll();

}