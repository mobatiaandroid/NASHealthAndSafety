package com.nas.healthandsafety.activity.evacuation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.evacuation.model.StudentModel
import com.nas.healthandsafety.constants.PreferenceManager


/**
 * Created by Arshad on 15,March,2022
 */

class StudentAdapter(
    val context: Context,
    var studentArray: ArrayList<StudentModel>,
    var studentRecycler: RecyclerView
) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(context)
            .inflate(R.layout.student_evacuation_adapter, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
        val item: StudentModel = studentArray[position]
        // Set the click listener for the item
        holder.itemView.setOnClickListener {
            Log.e("student name", item.fullName)
        }

        // Set the student's information
        holder.fullNameTextView.text = item.fullName
        holder.registrationIDTextView.text = item.registrationID

        // Set the state of the Switch
        holder.switchButton.setOnCheckedChangeListener(null) // Remove previous listener to avoid conflicts
        holder.switchButton.isChecked = item.evacuated == "1"

        // Set the text and background color based on the evacuated state
        val evacuatedText = if (item.evacuated == "1") "P" else "A"
        val evacuatedColor = if (item.evacuated == "1") R.color.green else R.color.pink
        holder.absentOrPresent.text = evacuatedText
        holder.absentOrPresent.setBackgroundColor(ContextCompat.getColor(context, evacuatedColor))

        // Set the listener for the Switch state change
        holder.switchButton.setOnCheckedChangeListener { _, isChecked ->
            val database = FirebaseDatabase.getInstance().reference
            val evacuatedRef = database.child("evacuation_students")
                .child(PreferenceManager.getFireRef(context))
                .child(studentArray[position].registrationID)
            evacuatedRef.child("evacuated").setValue(if (isChecked) 1 else 0)
            if (isChecked) {
                PreferenceManager.setScrollPos(context, position.toString())
                evacuatedRef.child("evacuated_assembly_points")
                    .setValue(PreferenceManager.getAssemblyPoint(context))
                evacuatedRef.child("evacuated_by").setValue(PreferenceManager.getStaffName(context))
            } else {
                PreferenceManager.setScrollPos(context, position.toString())
                evacuatedRef.child("evacuated_assembly_points")
                    .setValue("")
                evacuatedRef.child("evacuated_by").setValue("")
            }


            // Rest of the code for updating Firebase and UI based on the Switch state change
        }
    }

    override fun getItemCount(): Int {
        var size: Int = studentArray.size

        return studentArray.size


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullNameTextView = itemView.findViewById<TextView>(R.id.studentName)
        var registrationIDTextView = itemView.findViewById<TextView>(R.id.studentID)
        var switchButton = itemView.findViewById<Switch>(R.id.switch1)
        var absentOrPresent = itemView.findViewById<TextView>(R.id.presentOrAbsent)

    }
}


