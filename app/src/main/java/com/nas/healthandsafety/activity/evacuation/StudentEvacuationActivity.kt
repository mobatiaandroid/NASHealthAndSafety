package com.nas.healthandsafety.activity.evacuation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.evacuation.adapter.StudentAdapter
import com.nas.healthandsafety.activity.evacuation.model.StudentModel
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StudentEvacuationActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var progressBarDialog: ProgressBarDialog
    lateinit var firebaseID: String
    lateinit var firebaseReference: String

    //    lateinit var studentList: ArrayList<EvacuationStudentModel>
    lateinit var classNameTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var currentClassTextView: TextView
    lateinit var notFoundClassTextView: TextView
    lateinit var studentRecycler: RecyclerView
    private lateinit var database: DatabaseReference
    lateinit var studentArray: ArrayList<StudentModel>
    lateinit var studentAdapter: StudentAdapter
    lateinit var tabLayout: TabLayout


    //    lateinit var absentEvac: ArrayList<EvacuationStudentModel>
//    lateinit var presentEvac: ArrayList<EvacuationStudentModel>
    lateinit var subjectTextView: TextView
    var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_student_evacuation)
        studentArray = ArrayList()
        studentRecycler = findViewById(R.id.studentRecyclerView)
        dateTextView = findViewById(R.id.date)
        subjectTextView = findViewById(R.id.subject)
        classNameTextView = findViewById(R.id.className)
        currentClassTextView = findViewById(R.id.currentClassTextView)
        notFoundClassTextView = findViewById(R.id.notFoundClassTextView)

        currentClassTextView.setBackgroundResource(R.drawable.rounded_rectangle_green_disabled)
//        tabLayout = findViewById(R.id.tabLayout)
//        val firstTab : TabLayout.Tab = tabLayout.newTab()
//        firstTab.setText("Current Class"); // set the Text for the first
//        val secondTab: TabLayout.Tab = tabLayout.newTab()
//        secondTab.setText("NOT FOUND")
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("E MMM dd yyyy")
        val currentDate = current.format(formatter)
        dateTextView.text = currentDate
        subjectTextView.text = PreferenceManager.getSubject(context)
        classNameTextView.text = PreferenceManager.getClassName(context)
        studentRecycler.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
//        database = FirebaseDatabase.getInstance().reference
        database = FirebaseDatabase.getInstance().getReference("evacuation_students")
            .child(PreferenceManager.getFireRef(context))
        Log.e("ref", database.toString())
        currentClassTextView.setOnClickListener {
            currentClassTextView.setBackgroundResource(R.drawable.rounded_rectangle_green_disabled)
            notFoundClassTextView.setBackgroundResource(0)
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studentArray = ArrayList()

                        for (i in snapshot.children) {
                            val studentName = i.child("student_name").value.toString()
                            val registrationID = i.child("student_id").value.toString()
                            val section = i.child("student_class_section").value.toString()
                            val evacuated = i.child("evacuated").value.toString()
                            val temp = StudentModel(studentName, registrationID, section, evacuated)
                            Log.e("class", PreferenceManager.getClassName(context))
                            if (PreferenceManager.getClassName(context).contains(section)) {
                                studentArray.add(temp)
                            }


                        }
                    }
                    if (studentArray.isEmpty()) {
//                        Toast.makeText(context, "No students available", Toast.LENGTH_SHORT).show()

                        studentAdapter = StudentAdapter(context, ArrayList())
                        studentRecycler.adapter = studentAdapter
                    } else {
                        if (studentArray.size > 10) {
                            val subArrayList = ArrayList(studentArray.subList(0, 10))
                            studentArray = subArrayList
                        }
                        studentAdapter = StudentAdapter(context, studentArray)
                        studentRecycler.adapter = studentAdapter
                    }
                    Log.e("size", studentArray.size.toString())

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("err", "error")
                }

            })

        }
        notFoundClassTextView.setOnClickListener {
            currentClassTextView.setBackgroundResource(0)
            notFoundClassTextView.setBackgroundResource(R.drawable.rounded_rectangle_green_disabled)
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studentArray = ArrayList()
                        for (i in snapshot.children) {
                            val studentName = i.child("student_name").value.toString()
                            val registrationID = i.child("student_id").value.toString()
                            val section = i.child("student_class_section").value.toString()
                            val evacuated = i.child("evacuated").value.toString()
                            val temp = StudentModel(studentName, registrationID, section, evacuated)
                            Log.e("class", PreferenceManager.getClassName(context))
//                            if (section == PreferenceManager.getClassName(context)){
                            studentArray.add(temp)
//                            }


                        }
                    }
                    if (studentArray.isEmpty()) {
//                        Toast.makeText(context, "No students available", Toast.LENGTH_SHORT).show()
                        studentAdapter = StudentAdapter(context, ArrayList())
                        studentRecycler.adapter = studentAdapter
                    } else {
                        if (studentArray.size > 10) {
                            val subArrayList = ArrayList(studentArray.subList(0, 10))
                            studentArray = subArrayList
                        }
                        studentAdapter = StudentAdapter(context, studentArray)
                        studentRecycler.adapter = studentAdapter
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("err", "error")
                }

            })


        }
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    studentArray = ArrayList()
                    for (i in snapshot.children) {
                        val studentName = i.child("student_name").value.toString()
                        val registrationID = i.child("student_id").value.toString()
                        val section = i.child("student_class_section").value.toString()
                        val evacuated = i.child("evacuated").value.toString()
                        val temp = StudentModel(studentName, registrationID, section, evacuated)
                        Log.e("class", PreferenceManager.getClassName(context))
                        Log.e("class", PreferenceManager.getClassName(context))
                        if (PreferenceManager.getClassName(context).contains(section)) {
                            studentArray.add(temp)
                        }


                    }
                }
                if (studentArray.isEmpty()) {
//                    Toast.makeText(context, "No students available", Toast.LENGTH_SHORT).show()
                    studentAdapter = StudentAdapter(context, ArrayList())
                    studentRecycler.adapter = studentAdapter
                } else {
                    if (studentArray.size > 10) {
                        val subArrayList = ArrayList(studentArray.subList(0, 10))
                        studentArray = subArrayList
                    }
                    studentAdapter = StudentAdapter(context, studentArray)
                    studentRecycler.adapter = studentAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("err","error")
            }

        })

    }
}