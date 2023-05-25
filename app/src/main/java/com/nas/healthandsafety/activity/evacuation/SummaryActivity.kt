package com.nas.healthandsafety.activity.evacuation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nas.healthandsafety.R
import com.nas.healthandsafety.constants.PreferenceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SummaryActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var dateTextView: TextView
    lateinit var classNameTextView: TextView
    lateinit var subjectTextView: TextView
    lateinit var assemblyPointTextView: TextView
    lateinit var totalStudentsTextView: TextView
    lateinit var foundStudentsTextView: TextView
    lateinit var notFoundStudentsTextView: TextView
    lateinit var currentClassCountTextView: TextView
    lateinit var otherClassCountTextView: TextView
    lateinit var backImageView: ImageView
    private lateinit var database: DatabaseReference
    var totalStudentsCount = 0
    var foundStudentsCount = 0
    var notFoundStudentsCount = 0
    var currentClassCount = 0
    var otherClassCount = 0

    override fun onBackPressed() {
        val intent = Intent(context, StudentEvacuationActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        context = this
        initialiseUI()
    }

    private fun initialiseUI() {
        dateTextView = findViewById(R.id.date)
        classNameTextView = findViewById(R.id.className)
        subjectTextView = findViewById(R.id.subject)
        assemblyPointTextView = findViewById(R.id.assemblyPointName)
        totalStudentsTextView = findViewById(R.id.totalStudents)
        foundStudentsTextView = findViewById(R.id.presentStudents)
        notFoundStudentsTextView = findViewById(R.id.absentStudents)
        backImageView = findViewById(R.id.back_button)
        currentClassCountTextView = findViewById(R.id.currentClassCount)
        otherClassCountTextView = findViewById(R.id.otherClassCount)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("E MMM dd yyyy")
        val currentDate = current.format(formatter)
        dateTextView.text = currentDate
        classNameTextView.text = PreferenceManager.getClassName(context)
        subjectTextView.text = PreferenceManager.getSubject(context)
        assemblyPointTextView.text = PreferenceManager.getAssemblyPoint(context)

        database = FirebaseDatabase.getInstance().reference
        database = FirebaseDatabase.getInstance().getReference("evacuation_students")
            .child(PreferenceManager.getFireRef(context))
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    totalStudentsCount = 0
                    foundStudentsCount = 0
                    notFoundStudentsCount = 0
                    currentClassCount = 0
                    otherClassCount = 0
                    totalStudentsCount = snapshot.childrenCount.toInt()
                    for (i in snapshot.children) {
                        if (i.child("evacuated").value.toString() == "1") {
                            foundStudentsCount += 1

                            if (!i.child("evacuated_assembly_points").value.toString()
                                    .equals(i.child("student_assembly_points").value.toString())
                            ) {
                                otherClassCount += 1
                            }


                        }
                        if (PreferenceManager.getClassName(context).contains(
                                i.child("student_class_section").value.toString().replace(" ", "")
                            )
                        ) {
                            if (i.child("evacuated_assembly_points").value.toString()
                                    .equals(i.child("student_assembly_points").value.toString())
                            ) {
                                currentClassCount += 1
                            }
                        }

                    }
                    notFoundStudentsCount = totalStudentsCount - foundStudentsCount
//                    otherClassCount = totalStudentsCount - currentClassCount
                    totalStudentsTextView.text = totalStudentsCount.toString()
                    foundStudentsTextView.text = foundStudentsCount.toString()
                    notFoundStudentsTextView.text = notFoundStudentsCount.toString()
                    currentClassCountTextView.text = currentClassCount.toString()
                    otherClassCountTextView.text = otherClassCount.toString()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("err", "error")
            }

        })
        backImageView.setOnClickListener {
            val intent = Intent(context, StudentEvacuationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

    }
}