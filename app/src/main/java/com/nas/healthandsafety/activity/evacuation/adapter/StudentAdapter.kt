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
        if (item.evacuated == "1") {
            holder.switchButton.isChecked = true
            holder.absentOrPresent!!.text = "P"
            holder.absentOrPresent!!.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.green
                )
            )

        } else {
            holder.switchButton.isChecked = false
            holder.absentOrPresent!!.text = "A"
            holder.absentOrPresent!!.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.pink
                )
            )
            }


        /*Log.e("designtion",employee_array.get(position).designation.toString())*/


        holder.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->

            /*database= FirebaseDatabase.getInstance().getReference("StudentLists").
            child(employee_array.get(position).name.toString()).child("status")*/
            var database: DatabaseReference
            var id = ""
            if (isChecked) {
                val database = FirebaseDatabase.getInstance().reference
//
                val evacuatedRef = database.child("evacuation_students")
                    .child(PreferenceManager.getFireRef(context))
                    .child(studentArray[position].registrationID).child("evacuated")
//                                    val evacuatedRef = database.child("evacuation_students/"+PreferenceManager.getFireRef(context)+"/"+id+"/evacuated")
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
//                database = FirebaseDatabase.getInstance().getReference("evacuation_students")
//                    .child(PreferenceManager.getFireRef(context))
//                database.addValueEventListener(object :ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()){
//                            for (i in snapshot.children) {
//                                val fullName = i.child("student_name").value.toString()
//                                if (fullName == item.fullName) {
//                                    id = i.child("student_id").value.toString()
//
//                                    Log.e("Akam",id.toString())
//                                    val database = FirebaseDatabase.getInstance().reference
////
//                                    val evacuatedRef = database.child("evacuation_students").child(PreferenceManager.getFireRef(context)).child(id).child("evacuated")
////                                    val evacuatedRef = database.child("evacuation_students/"+PreferenceManager.getFireRef(context)+"/"+id+"/evacuated")
//                                    Log.e("evacuated ref", evacuatedRef.toString())
//                                    evacuatedRef.setValue("1")
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e("wha","pls no print")
//                    }
//
//                })
//                database = database.child(id)
//                val database = FirebaseDatabase.getInstance().reference
//                val evacuatedRef = database.child("evacuation_students/"+PreferenceManager.getFireRef(context)+"/"+id+"/evacuated")
//                Log.e("evacuated ref", evacuatedRef.toString())
//                evacuatedRef.setValue("1")
                Log.e("path to present", database.toString())
//                database.child("evacuated").setValue("1").addOnCompleteListener {
//                    Toast.makeText(
//                        context,
//                        "Changed",
//                        Toast.LENGTH_SHORT
//                    ).show() }
                Log.e("Child present", item.fullName)
                Log.e("path to absent", database.toString())

                Log.e("Checked_Success", isChecked.toString())
                holder.absentOrPresent!!.text = "P"
                holder.absentOrPresent!!.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
                holder.switchButton.isChecked = true

                /*database.setValue("0")
                        holder.switch_button.isChecked=true*/

            } else {
                val database = FirebaseDatabase.getInstance().reference
//
                val evacuatedRef = database.child("evacuation_students")
                    .child(PreferenceManager.getFireRef(context))
                    .child(studentArray[position].registrationID).child("evacuated")
//                                    val evacuatedRef = database.child("evacuation_students/"+PreferenceManager.getFireRef(context)+"/"+id+"/evacuated")
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
//                database = FirebaseDatabase.getInstance().getReference("evacuation_students")
//                    .child(PreferenceManager.getFireRef(context))
//                database = database.child(item.evacuated)
//                database.addValueEventListener(object :ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()){
//                            for (i in snapshot.children) {
//                                val fullName = i.child("student_name").value.toString()
//                                if (fullName == item.fullName) {
//                                    id = i.child("student_id").value.toString()
//
//                                    Log.e("Akam",id.toString())
//                                    val database = FirebaseDatabase.getInstance().reference
////
//                                    val evacuatedRef = database.child("evacuation_students").child(PreferenceManager.getFireRef(context)).child(id).child("evacuated")
////                                    val evacuatedRef = database.child("evacuation_students/"+PreferenceManager.getFireRef(context)+"/"+id+"/evacuated")
//                                    Log.e("evacuated ref", evacuatedRef.toString())
//                                    evacuatedRef.setValue("0")
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e("wha","pls no print")
//                    }
//
//                })
//                val database = FirebaseDatabase.getInstance().reference
//                val evacuatedRef = database.child("evacuation_students/"+PreferenceManager.getFireRef(context)+"/"+id+"/evacuated")
//                evacuatedRef.setValue("0")
                Log.e("checked_error", isChecked.toString())
                holder.absentOrPresent!!.text = "A"
                holder.absentOrPresent!!.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pink
                    )
                )
                holder.switchButton.isChecked = false
                /*database.setValue("1")
                            holder.switch_button.isChecked=false*/

            }


        }


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
        var switchButton = itemView.findViewById<Switch>(R.id.switch1)
        var absentOrPresent = itemView.findViewById<TextView>(R.id.presentOrAbsent)

    }
}


