package com.example.studentmanroom.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studentmanroom.models.StudentModel

@Dao
interface StudentDao {
    @Query("select * from students")
    fun getAllStudents(): Array<StudentModel>

    @Query("select * from students where _id = :id")
    fun getStudentById(id: Int): StudentModel?

    @Query("select count(*) from students where id = :mssv")
    fun isIdExists(mssv: String): Int

    @Insert
    fun insertStudent(student: StudentModel): Long

    @Update
    fun updateStudent(student: StudentModel): Int

    @Delete
    fun deleteStudent(student: StudentModel): Int
}