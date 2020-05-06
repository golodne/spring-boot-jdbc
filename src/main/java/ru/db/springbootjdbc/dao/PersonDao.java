package ru.db.springbootjdbc.dao;

import ru.db.springbootjdbc.model.Person;

public interface PersonDao {
    int count();
    void insert(Person person);
    Person getById(int id);
}
