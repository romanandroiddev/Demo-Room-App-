package com.example.roomdatabaseapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT * FROM students WHERE id=:id")
    suspend fun getStudentById(id: Int): Student

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM students WHERE name LIKE '%' || :searchText || '%' OR surname  LIKE '%' || :searchText || '%' ")
    suspend fun searchStudentByName(searchText: String): List<Student>
}