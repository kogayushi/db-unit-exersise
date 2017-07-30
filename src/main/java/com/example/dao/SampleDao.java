package com.example.dao;
import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import com.example.entity.Sample;

@Dao
@ConfigAutowireable
public interface SampleDao {

    @Select
    List<Sample> findAll();

    @Select
    Sample find(int id);

    @Insert
    int save(Sample s);
}