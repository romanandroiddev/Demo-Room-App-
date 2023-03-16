package com.example.roomdatabaseapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdatabaseapp.data.dao.StudentsDao
import com.example.roomdatabaseapp.data.models.Student


@Database(entities = [Student::class], version = 2)
abstract class StudentsDatabase() : RoomDatabase() {


    abstract fun getStudentsDao(): StudentsDao

    companion object {
        const val DATABASE_NAME = "db_name"


        fun getInstance(context: Context): StudentsDatabase {
            return Room.databaseBuilder(
                context, StudentsDatabase::class.java, DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }


    }
}