package com.nas.healthandsafety.activity.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.home.model.StudentsResponseModel


class StudentListAdapter(
    val context: Context,
    var studentArray: ArrayList<StudentsResponseModel.Data>
) :
    RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(context)
            .inflate(R.layout.student_list_item, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.fullNameTextView.text = studentArray[position].full_name
        holder.registrationIDTextView.text = studentArray[position].id.toString()


        /*Log.e("designtion",employee_array.get(position).designation.toString())*/


    }

    override fun getItemCount(): Int {
        /* Log.e("emplyee_list_count", employee_array.size.toString())*/
        var size: Int = studentArray.size
        /*Log.e("size",size.toString())*/
        if (size == 0) {
            Toast.makeText(context, "No Students Available", Toast.LENGTH_SHORT).show()
        }
        return studentArray.size


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullNameTextView = itemView.findViewById<TextView>(R.id.studentName)
        var registrationIDTextView = itemView.findViewById<TextView>(R.id.studentID)


    }
}


