package com.hei.wallet.heiwallet.fjpa;

import java.sql.SQLException;
import java.util.List;

public interface BasicCrudOperation <T>{
    T findById(Object id) throws SQLException;
    List<T> findAll() throws SQLException;
    T saveOrUpdate(T toSave) throws SQLException;
    List<T> saveOrUpdateAll(List<T> toSaves) throws SQLException;
}
