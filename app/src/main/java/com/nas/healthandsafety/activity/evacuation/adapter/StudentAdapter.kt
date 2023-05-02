package com.nas.healthandsafety.activity.evacuation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.evacuation.model.StudentModel
import com.nas.healthandsafety.constants.PreferenceManager


/**
 * Created by Arshad on 15,March,2022
 */

class StudentAdapter(val context: Context, var studentArray: ArrayList<StudentModel>) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(context)
            .inflate(R.layout.student_evacuation_adapter, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item: StudentModel = studentArray.get(position)
        holder.fullNameTextView.setText(item.fullName)
        holder.registrationIDTextView.setText(item.registrationID)

            if (item.evacuated == "1")
            {
                holder.switchButton.isChecked = true
                holder.absentOrPresent!!.text = "P"
                holder.absentOrPresent!!.setBackgroundColor(ContextCompat.getColor(context,R.color.green))

            } else
            {
                holder.switchButton.isChecked = false
                holder.absentOrPresent!!.text = "A"
                holder.absentOrPresent!!.setBackgroundColor(ContextCompat.getColor(context,R.color.pink))
            }


        /*Log.e("designtion",employee_array.get(position).designation.toString())*/


        holder.switchButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            /*database= FirebaseDatabase.getInstance().getReference("StudentLists").
            child(employee_array.get(position).name.toString()).child("status")*/
            var database: DatabaseReference
            if (isChecked)
            {
                database = FirebaseDatabase.getInstance().getReference("evacuation_students").child(PreferenceManager.getFireRef(context))
                    .child(item.evacuated)
                database.child("evacuated").setValue("1")
                Log.e("Checked_Success",isChecked.toString())
                holder.absentOrPresent!!.text = "P"
                holder.absentOrPresent!!.setBackgroundColor(ContextCompat.getColor(context,R.color.green))
                holder.switchButton.isChecked = true

                /*database.setValue("0")
                        holder.switch_button.isChecked=true*/

            }
            else
            {
                database = FirebaseDatabase.getInstance().getReference("evacuation_students").child(PreferenceManager.getFireRef(context))
                    .child(item.evacuated)
                database.child("evacuated").setValue("0")
                Log.e("checked_error",isChecked.toString())
                holder.absentOrPresent!!.text = "A"
                holder.absentOrPresent!!.setBackgroundColor(ContextCompat.getColor(context,R.color.pink))
                holder.switchButton.isChecked = false
                /*database.setValue("1")
                            holder.switch_button.isChecked=false*/

            }


        })


    }

    override fun getItemCount(): Int {
        /* Log.e("emplyee_list_count", employee_array.size.toString())*/
        var size: Int = studentArray.size
        /*Log.e("size",size.toString())*/
        return studentArray.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullNameTextView = itemView.findViewById<TextView>(R.id.studentName)
        var registrationIDTextView = itemView.findViewById<TextView>(R.id.studentID)
        var switchButton = itemView.findViewById<Switch>(R.id.switch1)
        var absentOrPresent = itemView.findViewById<TextView>(R.id.presentOrAbsent)

    }
}


