package com.example.prm4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.prm4.data.model.KierowcaEntity

@Database(
    entities = [KierowcaEntity::class],
    version = 1
)
abstract class KierowcaDatabase : RoomDatabase() {
    abstract val kierowcy: KierowcaDao

    companion object {
        fun open(context: Context): KierowcaDatabase = Room.databaseBuilder(
            context, KierowcaDatabase::class.java, "kierowcy.db"
        ).build()
    }
}