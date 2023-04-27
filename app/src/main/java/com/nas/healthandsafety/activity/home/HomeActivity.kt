package com.nas.healthandsafety.activity.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.attendance.AttendanceActivity
import com.nas.healthandsafety.activity.evacuation.StudentEvacuationActivity
import com.nas.healthandsafety.activity.fire_marshall.FireMarshallHomeActivity
import com.nas.healthandsafety.activity.gallery.GalleryActivity
import com.nas.healthandsafety.activity.profile.ProfileActivity
import com.nas.healthandsafety.activity.session_select.SessionSelectActivity
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import com.ncorti.slidetoact.SlideToActView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeActivity : AppCompatActivity() {
    //    lateinit var bottomNav: BottomNavigationCircles
    lateinit var context: Context
    lateinit var attendenceButton: ImageView
    lateinit var myProfile: ImageView
    lateinit var gallery: ImageView
    lateinit var extras: Bundle
    lateinit var classID: String
    lateinit var staffName: TextView
    lateinit var imageA: ImageView
    lateinit var imageB: ImageView
    lateinit var imageC: ImageView
    lateinit var count: TextView
    lateinit var greeting: TextView
    lateinit var totalStudents: TextView
    lateinit var presentStudents: TextView
    lateinit var absentStudents: TextView
    lateinit var subject: TextView
    lateinit var progressBarPresent: ProgressBar
    lateinit var progressBarAbsent: ProgressBar
    lateinit var className: TextView
    lateinit var date: TextView
    lateinit var assemblyAreaSelector: View
    lateinit var area: TextView
    lateinit var slider: SlideToActView
    lateinit var evacuateButton: View
    lateinit var fireMarshall: FloatingActionButton

    //    lateinit var presentStudentList: ArrayList<com.nas.fireevacuation.activity.staff_home.model.students_model.Lists>
//    lateinit var absentStudentList: ArrayList<com.nas.fireevacuation.activity.staff_home.model.students_model.Lists>
    var progressBarDialog: ProgressBarDialog? = null
    private fun greetingSetter() {
        val date = Date()
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
        if (hour in 6..12) {
            greeting.text = "Good Morning !"
        } else if (hour == 12) {
            greeting.text = "Noon !"
        } else if (hour in 13..16) {
            greeting.text = "Good Afternoon !"
        } else if (hour in 16..22) {
            greeting.text = "Good Evening !"
        } else {
            greeting.text = "Good Night !"
        }
    }

    //    var database = Firebase.database
//    var reference = database.reference
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        context = this
        progressBarDialog = ProgressBarDialog(context)
        attendenceButton = findViewById(R.id.attendence)
        fireMarshall = findViewById(R.id.fireMarshallButton)
        gallery = findViewById(R.id.gallery)
        myProfile = findViewById(R.id.myProfile)
        staffName = findViewById(R.id.staffName)
        imageA = findViewById(R.id.imageA)
        imageB = findViewById(R.id.imageB)
        imageC = findViewById(R.id.imageC)
        count = findViewById(R.id.count)
        totalStudents = findViewById(R.id.totalStudents)
        presentStudents = findViewById(R.id.presentStudents)
        absentStudents = findViewById(R.id.absentStudents)
        progressBarPresent = findViewById(R.id.progressBarPresent)
        progressBarAbsent = findViewById(R.id.progressBarAbsent)
        className = findViewById(R.id.className)
        subject = findViewById(R.id.subjectName)
        greeting = findViewById(R.id.greeting)
        date = findViewById(R.id.date)
        assemblyAreaSelector = findViewById(R.id.assemblyAreaSelector)
        area = findViewById(R.id.area)
        slider = findViewById(R.id.slider)
        evacuateButton = findViewById(R.id.evacuateButton)
//    var slideCompleteListener: OnSlideCompleteListener
//    presentStudentList = ArrayList()
//    absentStudentList = ArrayList()
        Log.e("token", PreferenceManager.getFCMID(context))
        fireMarshall.setOnClickListener {
            val intent = Intent(context, FireMarshallHomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        gallery.setOnClickListener {
            val intent = Intent(context, GalleryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        myProfile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        slider.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                val intent = Intent(context, SessionSelectActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }
        }
        evacuateButton.setOnClickListener {
            if (area.text.equals("Select Assembly Point")) {
                AppUtils.showMessagePopUp(context, "Please Select Assembly Point")
            } else {
//                evacuationCall()
                val intent = Intent(context, StudentEvacuationActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }
        }
        assemblyAreaSelector.setOnClickListener {
//        var assemblyPointsResponse: AssemblyPointsModel
//        var assemblyPointsList: ArrayList<Lists> = ArrayList()
            var i: Int = 0
            if (AppUtils.isInternetAvailable(context)) {
//            TODO
                // assembly points
//            val call: Call<AssemblyPointsModel> = ApiClient.getClient.assemblyPoints(
//                PreferenceManager.getAccessToken(context)
//            )
//            call.enqueue(object : Callback<AssemblyPointsModel> {
//                override fun onResponse(
//                    call: Call<AssemblyPointsModel>,
//                    response: Response<AssemblyPointsModel>
//                ) {
////                    Log.e("Assembly Response",response.body().toString())
//                    if (!response.body()!!.equals("")) {
//                        assemblyPointsResponse = response.body()!!
//                        if (assemblyPointsResponse.responsecode.equals("100")) {
//                            if (assemblyPointsResponse.message.equals("success")) {
//                                while (i < assemblyPointsResponse.data.lists.size) {
//                                    assemblyPointsList.add(assemblyPointsResponse.data.lists[i])
//                                    i++
//                                }
////                                Log.e("Assembly Points2", assemblyPointsList.toString())
//                                var i = 0
//                                var assemblyPointsStringList: ArrayList<String> = ArrayList()
//                                while (i < assemblyPointsList.size) {
//                                    assemblyPointsStringList.add(assemblyPointsList[i].assembly_point)
//                                    i++
//                                }
////                                Log.e("Assembly Points3", assemblyPointsStringList.toString())
//                                val builder = AlertDialog.Builder(context)
//                                builder.setTitle("Select Session")
//                                var checkedItem = -1
//                                builder.setSingleChoiceItems(
//                                    assemblyPointsStringList.toTypedArray(),
//                                    checkedItem
//                                ) { dialog, which ->
//                                    checkedItem = which
//                                }
//                                builder.setPositiveButton("OK") { dialog, which ->
//                                    if (checkedItem == -1) {
//                                        AppUtils.showMessagePopUp(
//                                            context,
//                                            "Please Select"
//                                        )
//                                    } else {
//                                        area.text = assemblyPointsStringList[checkedItem]
//                                        PreferenceManager.setAssemblyPoint(
//                                            context,
//                                            assemblyPointsList[checkedItem].id
//                                        )
//
//                                    }
//
//                                }
//                                builder.setNegativeButton("Cancel", null)
//                                val dialog = builder.create()
//                                dialog.show()
//
//                            }
//                        } else if (assemblyPointsResponse.responsecode.equals("402")) {
//                            AppUtils.showMessagePopUp(context, "Session Expired")
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<AssemblyPointsModel>, t: Throwable) {
//                    AppUtils.showMessagePopUp(context, "Some Error Occurred")
//                }
//
//            })
            } else {
                AppUtils.showMessagePopUp(context, "Check your Internet connection")
            }

        }
        attendenceButton.setOnClickListener {
            val intent = Intent(context, AttendanceActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("E MMM dd yyyy")
        val currentDate = current.format(formatter)
        greetingSetter()
        date.text = currentDate
        classID = PreferenceManager.getClassID(context)
        className.text = PreferenceManager.getClassName(context)
        staffName.text = PreferenceManager.getStaffName(context)
        subject.text = PreferenceManager.getSubject(context)
        Log.e("Class ID:", classID)
//    TODO
//    studentListCall(classID)

//        bottomNav = findViewById(R.id.bottom_nav)

//        bottomNav.backgroundTintBlendMode = BlendMode.DIFFERENCE
//        bottomNav.foregroundTintBlendMode = BlendMode.DIFFERENCE
//        bottomNav.circleColor = Color.RED


    }
}