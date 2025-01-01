package com.example.studentmanroom.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentModel(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    var id: String,
    var name: String,
)