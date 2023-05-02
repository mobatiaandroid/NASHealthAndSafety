package com.nas.healthandsafety.activity.evacuation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.evacuation.adapter.StudentAdapter
import com.nas.healthandsafety.activity.evacuation.model.StudentModel
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog

class StudentEvacuationActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var progressBarDialog: ProgressBarDialog
    lateinit var firebaseID: String
    lateinit var firebaseReference: String
//    lateinit var studentList: ArrayList<EvacuationStudentModel>
    lateinit var className: TextView
    lateinit var date: TextView
    lateinit var studentRecycler: RecyclerView
    private lateinit var database: DatabaseReference
    lateinit var studentArray: ArrayList<StudentModel>
    lateinit var studentAdapter: StudentAdapter


    //    lateinit var absentEvac: ArrayList<EvacuationStudentModel>
//    lateinit var presentEvac: ArrayList<EvacuationStudentModel>
    lateinit var subject: TextView
    var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_student_evacuation)
        studentArray = ArrayList()
        studentRecycler = findViewById(R.id.studentRecyclerView)
        studentRecycler.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL, false)
        database = FirebaseDatabase.getInstance().reference
        database=FirebaseDatabase.getInstance().getReference("evacuation_students").child(PreferenceManager.getFireRef(context))
        Log.e("ref",database.toString())
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    studentArray = ArrayList()
                    for (i in snapshot.children) {
                        val fullName = i.child("full_name").value.toString()
                        val registrationID = i.child("registration_id").value.toString()
                        val section = i.child("section").value.toString()
                        val evacuated = i.child("evacuated").value.toString()
                        val temp = StudentModel(fullName, registrationID, section, evacuated)
                        studentArray.add(temp)
                    }
                }
                Log.e("size",studentArray.size.toString())
                studentAdapter = StudentAdapter(context, studentArray)
                studentRecycler.adapter = studentAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("err","error")
            }

        })

    }
}