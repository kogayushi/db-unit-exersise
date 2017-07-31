package com.example.dao;
import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import com.example.entity.DbUnitSample;

@Dao
@ConfigAutowireable
public interface DbUnitSampleDao {

    @Select
    List<DbUnitSample> findAll();

    @Select
    DbUnitSample find(int id);

    @Insert
    int save(DbUnitSample s);
}