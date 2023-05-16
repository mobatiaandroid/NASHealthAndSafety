package com.nas.healthandsafety.activity.evacuation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
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
        holder.itemView.setOnClickListener {
            Log.e("student name", item.fullName)

        }
        holder.fullNameTextView.text = item.fullName
        holder.registrationIDTextView.text = item.registrationID
        holder.switchButton.isChecked = item.evacuated == "1"

        if (item.evacuated == "1") {
//            holder.switchButton.isChecked = true
            holder.absentOrPresent!!.text = "P"
            holder.absentOrPresent!!.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.green
                )
            )

        } else {
//            holder.switchButton.isChecked = false
            holder.absentOrPresent!!.text = "A"
            holder.absentOrPresent!!.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.pink
                )
            )
            }




        holder.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->

            var database: DatabaseReference
            var id = ""
            if (isChecked) {
                val database = FirebaseDatabase.getInstance().reference
//
                val evacuatedRef = database.child("evacuation_students")
                    .child(PreferenceManager.getFireRef(context))
                    .child(studentArray[position].registrationID).child("evacuated")
                Log.e("evacuated ref", evacuatedRef.toString())
                evacuatedRef.setValue("1")
                val staffRef = database.child("evacuation_students")
                    .child(PreferenceManager.getFireRef(context))
                    .child(studentArray[position].registrationID).child("evacuated_by")
                staffRef.setValue(PreferenceManager.getStaffName(context))
                Log.e("staffname", PreferenceManager.getStaffName(context))

                val assemblyRef = database.child("evacuation_students")
                    .child(PreferenceManager.getFireRef(context))
                    .child(studentArray[position].registrationID).child("evacuated_assembly_points")
                assemblyRef.setValue(PreferenceManager.getAssemblyPoint(context))
                holder.absentOrPresent!!.text = "P"
                holder.absentOrPresent!!.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
                holder.switchButton.isChecked = true

            } else {
                val database = FirebaseDatabase.getInstance().reference
                val evacuatedRef = database.child("evacuation_students")
                    .child(PreferenceManager.getFireRef(context))
                    .child(studentArray[position].registrationID).child("evacuated")
                Log.e("evacuated ref", evacuatedRef.toString())
                evacuatedRef.setValue("0")
                val staffRef = database.child("evacuation_students")
                    .child(PreferenceManager.getFireRef(context))
                    .child(studentArray[position].registrationID).child("evacuated_by")
                staffRef.setValue(PreferenceManager.getStaffName(context))
                Log.e("staffname", PreferenceManager.getStaffName(context))
                val assemblyRef = database.child("evacuation_students")
                    .child(PreferenceManager.getFireRef(context))
                    .child(studentArray[position].registrationID).child("evacuated_assembly_points")
                assemblyRef.setValue(PreferenceManager.getAssemblyPoint(context))
                Log.e("checked_error", isChecked.toString())
                holder.absentOrPresent!!.text = "A"
                holder.absentOrPresent!!.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pink
                    )
                )
                holder.switchButton.isChecked = false
            }
        }
    }

    override fun getItemCount(): Int {
        var size: Int = studentArray.size
        if (size == 0) {
            Toast.makeText(context, "No Students Available", Toast.LENGTH_SHORT).show()
        }
        return studentArray.size


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullNameTextView = itemView.findViewById<TextView>(R.id.studentName)
        var registrationIDTextView = itemView.findViewById<TextView>(R.id.studentID)
        var switchButton = itemView.findViewById<Switch>(R.id.switch1)
        var absentOrPresent = itemView.findViewById<TextView>(R.id.presentOrAbsent)

    }
}


