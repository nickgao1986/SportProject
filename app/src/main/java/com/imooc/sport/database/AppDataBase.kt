package com.imooc.sport.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.nick.apps.pregnancy11.mypedometer.fragment.model.StepData

@Database(entities = arrayOf(StepData::class), version = 1)
abstract class AppDatabase : RoomDatabase(){

    //获取数据表操作实例
    abstract fun StepDao(): StepDao


    //单例数据库
    companion object {

        private var instance: AppDatabase? = null
        @Synchronized
        fun get(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "app_database")
                    .allowMainThreadQueries()
                   .build()
            }
            return instance!!
        }

    }

}
