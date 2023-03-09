package com.example.roomdatabaseapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.roomdatabaseapp.data.models.Student


@Dao
interface StudentsDao {
    @Query("SELECT * FROM students")
    suspend fun getListOfStudents(): List<Student>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStudent(student: Student)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStudent(student: Student)


    @Query("SELECT * FROM students WHERE name LIKE '%' || :name || '%'")
    suspend fun searchStudentByName(name: String): List<Student>
}