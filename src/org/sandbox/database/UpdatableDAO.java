package org.sandbox.database;

abstract public class UpdatableDAO<T extends Model> extends CreatableDAO<T> {

    abstract public boolean update(T obj);
}
