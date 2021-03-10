package com.example.projetandroid

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetandroid.Flux
import com.example.projetandroid.Info

@Database(entities = arrayOf(Flux::class,Info::class),version = 2,exportSchema = false)

abstract class DataBase :RoomDatabase(){
    abstract fun rss(): MyDao
    companion object
    {
        @Volatile
        private var INSTANCE: DataBase? =null
        fun getDatabase(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,DataBase::class.java,"MyDataBase")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }}
    }
}