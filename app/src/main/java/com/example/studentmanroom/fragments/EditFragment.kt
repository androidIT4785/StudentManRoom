package com.example.studentmanroom.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.studentmanroom.R

class EditFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        val editHoten = view.findViewById<EditText>(R.id.edit_hoten)
        val editMssv = view.findViewById<EditText>(R.id.edit_mssv)

        val name = arguments?.getString("name")
        val id = arguments?.getString("id")
        val pos = arguments?.getInt("pos")
        val _id = arguments?.getInt("_id")

        editHoten.setText(name)
        editMssv.setText(id)

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
                args.putInt("pos", pos!!)
                args.putInt("_id", _id!!)
                parentFragmentManager.setFragmentResult("edit", args)
                findNavController().navigateUp()
            }
        }

        return view
    }


}