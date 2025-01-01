package com.example.studentmanroom.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.studentmanroom.R
import androidx.navigation.fragment.findNavController

class AddFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        val editHoten = view.findViewById<EditText>(R.id.edit_hoten)
        val editMssv = view.findViewById<EditText>(R.id.edit_mssv)

        view.findViewById<Button>(R.id.button_cancel).setOnClickListener{
            findNavController().navigateUp()
        }

        view.findViewById<Button>(R.id.button_ok).setOnClickListener {
            val name = editHoten.text.toString().trim()
            val id = editMssv.text.toString().trim()
            if(name.isNotEmpty() && id.isNotEmpty()){
                val args = Bundle()
                args.putString("name", name)
                args.putString("id", id)
                parentFragmentManager.setFragmentResult("add", args)
                findNavController().navigateUp()
            }
        }


        return view
    }

}