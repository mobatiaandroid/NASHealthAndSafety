package com.nas.healthandsafety.activity.evacuation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.evacuation.adapter.StudentAdapter
import com.nas.healthandsafety.activity.evacuation.model.StudentModel
import com.nas.healthandsafety.activity.home.HomeActivity
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StudentEvacuationActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var progressBarDialog: ProgressBarDialog
    lateinit var firebaseID: String
    lateinit var firebaseReference: String
    lateinit var viewSummaryButton: MaterialButton
    lateinit var evacuationTitleTextView: TextView
//
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
    lateinit var searchIcon: ImageView
    lateinit var searchView: View
    lateinit var searchText: EditText
    lateinit var searchClose: ImageView
    lateinit var backButton: ImageView
    lateinit var searchRecyclerView: RecyclerView



    //    lateinit var absentEvac: ArrayList<EvacuationStudentModel>
//    lateinit var presentEvac: ArrayList<EvacuationStudentModel>
    lateinit var subjectTextView: TextView
    var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onBackPressed() {
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

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
        viewSummaryButton = findViewById(R.id.viewSummaryButton)
        currentClassTextView.setBackgroundResource(R.drawable.rounded_rectangle_green_disabled)
        evacuationTitleTextView = findViewById(R.id.evacuationTitle)
        searchIcon = findViewById(R.id.searchIcon)
        searchView = findViewById(R.id.searchView)
        searchText = findViewById(R.id.searchText)
        searchClose = findViewById(R.id.searchClose)
        backButton = findViewById(R.id.back_button)
        searchRecyclerView = findViewById(R.id.searchRecycler)
        searchRecyclerView.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        searchIcon.setOnClickListener {
            searchView.visibility = View.VISIBLE
            evacuationTitleTextView.visibility = View.GONE
            searchIcon.visibility = View.GONE
            backButton.visibility = View.GONE
        }
        searchClose.setOnClickListener {
            closeKeyboard()
            try {
                searchView.visibility = View.GONE
                evacuationTitleTextView.visibility = View.VISIBLE
                searchIcon.visibility = View.VISIBLE
                backButton.visibility = View.VISIBLE
//                val adapter = StudentAdapter(context, studentsArrayList,"ALL")
//                recyclerView.adapter = adapter
                searchText.text.clear()
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }

        }
        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 1) {
                    searchFilter(s.toString())
                }
            }

        })
        PreferenceManager.setScrollPos(context, "0")
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
        val layoutManager = studentRecycler.layoutManager
        val currentPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        viewSummaryButton.setOnClickListener {
            val intent = Intent(context, SummaryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
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
//                                if (temp.registrationID.toInt() < 10) {
                                studentArray.add(temp)
//                                }
                            }


                        }
                    }
                    studentRecycler.post {
                        studentRecycler.scrollToPosition(currentPosition)
                    }
                    if (studentArray.isEmpty()) {
//                        Toast.makeText(context, "No students available", Toast.LENGTH_SHORT).show()


                        studentAdapter = StudentAdapter(context, ArrayList(), studentRecycler)
                        studentRecycler.adapter = studentAdapter
                        studentRecycler.layoutManager!!.scrollToPosition(
                            PreferenceManager.getScrollPos(
                                context
                            ).toInt()
                        )
                    } else {
//                        if (studentArray.size > 10) {
//                            val subArrayList = ArrayList(studentArray.subList(0, 10))
//                            studentArray = subArrayList
//                        }
                        studentAdapter = StudentAdapter(context, studentArray, studentRecycler)
                        studentRecycler.adapter = studentAdapter
                        studentRecycler.layoutManager!!.scrollToPosition(
                            PreferenceManager.getScrollPos(
                                context
                            ).toInt()
                        )
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
//                            if (temp.registrationID.toInt() < 10) {
                            studentArray.add(temp)
//                            }
//                            }


                        }
                    }
                    studentRecycler.post {
                        studentRecycler.scrollToPosition(currentPosition)
                    }
                    if (studentArray.isEmpty()) {
//                        Toast.makeText(context, "No students available", Toast.LENGTH_SHORT).show()
                        studentAdapter = StudentAdapter(context, ArrayList(), studentRecycler)
                        studentRecycler.adapter = studentAdapter
                        studentRecycler.layoutManager!!.scrollToPosition(
                            PreferenceManager.getScrollPos(
                                context
                            ).toInt()
                        )
                    } else {
//                        if (studentArray.size > 10) {
//                            val subArrayList = ArrayList(studentArray.subList(0, 10))
//                            studentArray = subArrayList
//                        }
                        studentAdapter = StudentAdapter(context, studentArray, studentRecycler)
                        studentRecycler.adapter = studentAdapter
                        studentRecycler.layoutManager!!.scrollToPosition(
                            PreferenceManager.getScrollPos(
                                context
                            ).toInt()
                        )
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("err", "error")
                    Toast.makeText(context, "Errror Occurred", Toast.LENGTH_SHORT).show()

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
                        Log.e("class", section)
                        if (PreferenceManager.getClassName(context).contains(section)) {
//                            if (temp.registrationID.toInt() < 10) {
                                studentArray.add(temp)
//                            }

                        }


                    }
                }
                if (studentArray.isEmpty()) {
//                    Toast.makeText(context, "No students available", Toast.LENGTH_SHORT).show()
                    studentAdapter = StudentAdapter(context, ArrayList(), studentRecycler)
                    studentRecycler.adapter = studentAdapter
                    studentRecycler.layoutManager!!.scrollToPosition(
                        PreferenceManager.getScrollPos(
                            context
                        ).toInt()
                    )
                } else {
//                    if (studentArray.size > 10) {
//                        val subArrayList = ArrayList(studentArray.subList(0, 10))
//                        studentArray = subArrayList
//                    }
                    Log.e("student list size", studentArray.size.toString())
                    studentAdapter = StudentAdapter(context, studentArray, studentRecycler)
                    studentRecycler.adapter = studentAdapter
                    studentRecycler.layoutManager!!.scrollToPosition(
                        PreferenceManager.getScrollPos(
                            context
                        ).toInt()
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("err","error")
            }

        })

    }

    private fun searchFilter(searchText: String) {
        database = FirebaseDatabase.getInstance().getReference("evacuation_students")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    studentArray = ArrayList()
//                    for (i in snapshot.children) {
//                        val studentName = i.child("student_name").value.toString()
//                        val registrationID = i.child("student_id").value.toString()
//                        val section = i.child("student_class_section").value.toString()
//                        val evacuated = i.child("evacuated").value.toString()
//                        val temp = StudentModel(studentName, registrationID, section, evacuated)
//                        Log.e("class", PreferenceManager.getClassName(context))
////                            if (section == PreferenceManager.getClassName(context)){
////                            if (temp.registrationID.toInt() < 10) {
//                        studentArray.add(temp)
////                            }
////                            }
//
//
//                    }
//                }
//
//                if (studentArray.isEmpty()) {
////                        Toast.makeText(context, "No students available", Toast.LENGTH_SHORT).show()
//                    studentAdapter = StudentAdapter(context, ArrayList(), studentRecycler)
//                    studentRecycler.adapter = studentAdapter
//                    studentRecycler.layoutManager!!.scrollToPosition(
//                        PreferenceManager.getScrollPos(
//                            context
//                        ).toInt()
//                    )
//                } else {
////                        if (studentArray.size > 10) {
////                            val subArrayList = ArrayList(studentArray.subList(0, 10))
////                            studentArray = subArrayList
////                        }
//                    studentAdapter = StudentAdapter(context, studentArray, studentRecycler)
//                    studentRecycler.adapter = studentAdapter
//                    studentRecycler.layoutManager!!.scrollToPosition(
//                        PreferenceManager.getScrollPos(
//                            context
//                        ).toInt()
//                    )
//                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("err", "error")
                Toast.makeText(context, "Errror Occurred", Toast.LENGTH_SHORT).show()

            }

        })


//        var studentsResponse: StudentModel
//        var studentsArrayList: ArrayList<Lists> = ArrayList()
//        var filteredList: ArrayList<Lists> = ArrayList()
//        var i: Int = 0
//        val call: Call<StudentModel> = ApiClient.getClient.studentsAPICall(
//            PreferenceManager.getAccessToken(context),
//            PreferenceManager.getClassID(context)
//        )
//        progressBarDialog!!.show()
//        call.enqueue(object : Callback<StudentModel> {
//            override fun onResponse(call: Call<StudentModel>, response: Response<StudentModel>) {
//                progressBarDialog!!.hide()
//                if (!response.body()!!.equals("")) {
//                    studentsResponse = response.body()!!
//                    if (studentsResponse.responsecode.equals("100")) {
//                        if (studentsResponse.message.equals("success")) {
//                            while (i<studentsResponse.data.lists.size) {
//                                studentsArrayList.add(studentsResponse.data.lists[i])
//                                i++
//                            }
//                            for (item in studentsArrayList) {
//                                if (item.name.toLowerCase().contains(searchString.toLowerCase()) || item.registration_id.contains(searchString)) {
//                                    if (!filteredList.contains(item)) {
//                                        filteredList.add(item)
//
//                                    }
//                                }
//                                filteredList.sortBy {
//                                    it.name
//                                }
//
//
//
//                            }
//
//                            val adapter = StudentAdapter(context, filteredList,"ALL")
//                            recyclerView.adapter = adapter
//                            filteredList = ArrayList()
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<StudentModel>, t: Throwable) {
//                progressBarDialog!!.hide()
//            }
//
//        })
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}