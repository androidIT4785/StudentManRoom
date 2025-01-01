package com.example.studentmanroom.controllers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.studentmanroom.DAO.StudentDao
import com.example.studentmanroom.adapters.StudentAdapter
import com.example.studentmanroom.models.StudentModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteStudentController(val students: MutableList<StudentModel>,
                              val studentAdapter: StudentAdapter,
                              val context: Context,
                              val position: Int,
                              val studentList: ListView,
                              val studentDao: StudentDao,
                              val lifecycle: LifecycleCoroutineScope,
                              val loadStudents: () -> Unit
) {
    fun deleteStudent(){
        val student = students[position]
        AlertDialog.Builder(context)
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn có chắc muốn xóa sinh viên ${student.name} ?")
            .setPositiveButton("OK",{ dialog, _ ->
                lifecycle.launch(Dispatchers.IO){
                    val res = studentDao.deleteStudent(student)
                    loadStudents()
                }
                studentAdapter.notifyDataSetChanged()
                Snackbar.make(
                    studentList,
                    "Đã xóa ${student.name} - ${student.id}",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Undo"){
                        lifecycle.launch(Dispatchers.IO){
                            val res = studentDao.insertStudent(student)
                            loadStudents()
                        }
                        studentAdapter.notifyDataSetChanged()
                        if(position == 0 || position == students.size-1){
                            studentList.setSelection(position)
                        }
                    }.show()
            })
            .setNegativeButton("Hủy", null)
            .show()
    }
}