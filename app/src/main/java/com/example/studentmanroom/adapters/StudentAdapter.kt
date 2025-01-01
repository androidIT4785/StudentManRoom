package com.example.studentmanroom.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.studentmanroom.R
import com.example.studentmanroom.models.StudentModel

class StudentAdapter (val students: MutableList<StudentModel>): BaseAdapter() {
    override fun getCount(): Int = students.size

    override fun getItem(position: Int): Any = students[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: StudentViewHolder
        var itemView: View
        if(convertView == null){
            itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.list_item, parent, false)
            viewHolder = StudentViewHolder()
            viewHolder.studentName = itemView.findViewById(R.id.studentName)
            viewHolder.studentId = itemView.findViewById(R.id.studentId)
            itemView.tag = viewHolder
        }else{
            itemView = convertView
            viewHolder = itemView.tag as StudentViewHolder
        }
        viewHolder.studentName.text = students[position].name
        viewHolder.studentId.text = students[position].id

        return itemView
    }

    class StudentViewHolder{
        lateinit var studentName: TextView
        lateinit var studentId: TextView
    }

}