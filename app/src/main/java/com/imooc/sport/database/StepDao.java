package com.imooc.sport.database;

import android.arch.persistence.room.*;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.StepData;

import java.util.List;

/**
 * Created by Allen on 2018/4/16/016.
 */

@Dao
public interface StepDao {
    //所有的CURD根据primary key进行匹配
    //------------------------query------------------------
    @Query("SELECT * FROM StepData")
    List<StepData> getAll();

    @Query("SELECT * FROM StepData WHERE date = :date")
    StepData findByDate(String date);

    //-----------------------insert----------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(StepData user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(StepData... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<StepData> users);

    //---------------------update------------------------
    @Update()
    int update(StepData user);

    @Update()
    int updateAll(StepData... user);

    @Update()
    int updateAll(List<StepData> user);

    //-------------------delete-------------------
    @Delete
    int delete(StepData user);

    @Delete
    int deleteAll(List<StepData> users);

    @Delete
    int deleteAll(StepData... users);
}
