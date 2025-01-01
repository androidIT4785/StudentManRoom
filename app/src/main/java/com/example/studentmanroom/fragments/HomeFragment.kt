package com.example.studentmanroom.fragments

import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.studentmanroom.DAO.StudentDao
import com.example.studentmanroom.R
import com.example.studentmanroom.adapters.StudentAdapter
import com.example.studentmanroom.controllers.DeleteStudentController
import com.example.studentmanroom.database.StudentDatabase
import com.example.studentmanroom.models.StudentModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private val students = mutableListOf<StudentModel>()
    private lateinit var studentList: ListView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var studentDao: StudentDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        studentDao = StudentDatabase.getInstance(requireContext()).studentDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize ListView and adapter
        studentList = view.findViewById(R.id.studentList)
        studentAdapter = StudentAdapter(students)
        studentList.adapter = studentAdapter

        // Load data into the list
        loadStudents()

        // Listen for "add" result
        parentFragmentManager.setFragmentResultListener("add", this) { _, args ->
            val name = args.getString("name")
            val id = args.getString("id")

            lifecycleScope.launch(Dispatchers.IO) {
//                if (id != null && studentDao.isIdExists(id) > 0) {
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(requireContext(), "Mã sinh viên đã tồn tại", Toast.LENGTH_SHORT).show()
//                    }
//                } else
                if (name != null && id != null) {
                    val res = studentDao.insertStudent(StudentModel(name = name, id = id))
                    loadStudents()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Đã thêm sinh viên [$res]",
                            Toast.LENGTH_SHORT
                        ).show()
                        studentList.setSelection(0)
                    }
                }
            }
        }

        // Listen for "edit" result
        parentFragmentManager.setFragmentResultListener("edit", this) { _, args ->
            val name = args.getString("name")
            val id = args.getString("id")
            val _id = args.getInt("_id")

            if (name != null && id != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val res = studentDao.updateStudent(
                        StudentModel(_id = _id, name = name, id = id)
                    )
                    loadStudents()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Đã cập nhật sinh viên [$res]",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        registerForContextMenu(studentList)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                findNavController().navigate(R.id.action_homeFragment_to_addFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pos = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        when (item.itemId) {
            R.id.action_edit -> {
                val args = Bundle().apply {
                    putString("name", students[pos].name)
                    putString("id", students[pos].id)
                    putInt("_id", students[pos]._id)
                }
                findNavController().navigate(R.id.action_homeFragment_to_editFragment, args)
                return true
            }

            R.id.action_delete -> {
                DeleteStudentController(
                    students, studentAdapter,
                    requireContext(),
                    pos,
                    studentList,
                    studentDao,
                    this.lifecycleScope,
                    { loadStudents() }
                ).deleteStudent()
                true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun loadStudents() {
        lifecycleScope.launch(Dispatchers.IO) {
            val allStudents = studentDao.getAllStudents()
            withContext(Dispatchers.Main) {
                students.clear()
                students.addAll(allStudents)
                studentAdapter.notifyDataSetChanged()
            }
        }
    }
}
