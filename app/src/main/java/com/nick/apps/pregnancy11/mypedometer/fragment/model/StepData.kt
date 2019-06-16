package com.nick.apps.pregnancy11.mypedometer.fragment.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel


@Entity(tableName = "StepData", indices = [])
class StepData {

    //当天日期
    @PrimaryKey
    @ColumnInfo(name = "date")
    var date: String = "2019-06-16"
    //对应date时间的步数
    @ColumnInfo(name = "step")
    var step: Long = 0

    //目标步数
    //对应date时间的步数
    @ColumnInfo(name = "target")
    var target: Int = 0

    constructor() {

    }

    protected constructor(`in`: Parcel) {
        date = `in`.readString()
        step = `in`.readLong()
        target = `in`.readInt()
    }


    override fun equals(obj: Any?): Boolean {
        val data = obj as StepData?
        return if (this.date == data!!.date) {
            true
        } else false
    }

    override fun toString(): String {
        return "TodayStepData{" +
                ", date=" + date +
                ", step=" + step +
                ", target=" + target +
                '}'.toString()
    }

}
