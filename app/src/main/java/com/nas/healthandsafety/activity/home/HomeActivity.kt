package com.nas.healthandsafety.activity.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.attendance.AttendanceActivity
import com.nas.healthandsafety.activity.evacuation.StudentEvacuationActivity
import com.nas.healthandsafety.activity.fire_marshall.FireMarshallHomeActivity
import com.nas.healthandsafety.activity.gallery.GalleryActivity
import com.nas.healthandsafety.activity.home.model.EvacuationStatusResponseModel
import com.nas.healthandsafety.activity.home.model.StudentsResponseModel
import com.nas.healthandsafety.activity.login.SignInActivity
import com.nas.healthandsafety.activity.profile.ProfileActivity
import com.nas.healthandsafety.activity.session_select.SessionSelectActivity
import com.nas.healthandsafety.activity.session_select.model.YearGroupsResponseModel
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import com.ncorti.slidetoact.SlideToActView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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
    lateinit var countTextView: TextView
    lateinit var greeting: TextView
    lateinit var totalStudentsTextView: TextView
    lateinit var presentStudents: TextView
    lateinit var absentStudents: TextView
    lateinit var subjectTextView: TextView
    lateinit var progressBarPresent: ProgressBar
    lateinit var progressBarAbsent: ProgressBar
    lateinit var classNameTextView: TextView
    lateinit var date: TextView
    lateinit var assemblyAreaSelector: View
    lateinit var area: TextView
    lateinit var slider: SlideToActView
    lateinit var evacuateButton: View
    lateinit var fireMarshall: FloatingActionButton
    var studentArray: ArrayList<StudentsResponseModel.Data> = ArrayList()
    var isEvac = false
    var firebaseKey = ""

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
        countTextView = findViewById(R.id.count)
        totalStudentsTextView = findViewById(R.id.totalStudents)
        presentStudents = findViewById(R.id.presentStudents)
        absentStudents = findViewById(R.id.absentStudents)
        progressBarPresent = findViewById(R.id.progressBarPresent)
        progressBarAbsent = findViewById(R.id.progressBarAbsent)
        classNameTextView = findViewById(R.id.className)
        subjectTextView = findViewById(R.id.subjectName)
        greeting = findViewById(R.id.greeting)
        date = findViewById(R.id.date)
        assemblyAreaSelector = findViewById(R.id.assemblyAreaSelector)
        area = findViewById(R.id.area)
        slider = findViewById(R.id.slider)
        evacuateButton = findViewById(R.id.evacuateButton)
        subjectTextView.text = PreferenceManager.getSubject(context)
        classNameTextView.text = PreferenceManager.getClassName(context)
        callStudentsListAPI()
        callEvacuationStatusAPI()
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
            if (!isEvac) {
                AppUtils.showMessagePopUp(context, "No Evacuations are in progress")
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
        classNameTextView.text = PreferenceManager.getClassName(context)
        staffName.text = PreferenceManager.getStaffName(context)
        subjectTextView.text = PreferenceManager.getSubject(context)
//    TODO
//    studentListCall(classID)

//        bottomNav = findViewById(R.id.bottom_nav)

//        bottomNav.backgroundTintBlendMode = BlendMode.DIFFERENCE
//        bottomNav.foregroundTintBlendMode = BlendMode.DIFFERENCE
//        bottomNav.circleColor = Color.RED


    }

    private fun callEvacuationStatusAPI() {
        var evacuationStatusResponse: EvacuationStatusResponseModel
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<EvacuationStatusResponseModel> = ApiClient.getClient.getEvacuationStatus(
                "Bearer "+PreferenceManager.getAccessToken(context)
            )
            progressBarDialog!!.show()
            call.enqueue(object : Callback<EvacuationStatusResponseModel> {
                override fun onResponse(call: Call<EvacuationStatusResponseModel>, response: Response<EvacuationStatusResponseModel>) {
                    progressBarDialog!!.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context,getString(R.string.text_unknown_error))
                    } else {
                        evacuationStatusResponse = response.body()!!
                        if (evacuationStatusResponse.status == 200) {
                            if (evacuationStatusResponse.data!!.isNotEmpty()){
                                for (i in evacuationStatusResponse.data!!.indices){
                                    if (evacuationStatusResponse.data!![i]!!.status == 1){
                                        isEvac = true
                                        firebaseKey = evacuationStatusResponse.data!![i]!!.evacuation_id!!
                                        PreferenceManager.setFireRef(context, firebaseKey)
                                    }
                                }
                            }else{
                                AppUtils.showMessagePopUp(context,"No evacuations are in progress")
                            }
                        } else if(evacuationStatusResponse.status == 401) {
                            AppUtils.showMessagePopUp(context, "Unauthenticated or Token Expired, Please Login")
                        } else {
                            AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                        }
                    }

                }
                override fun onFailure(call: Call<EvacuationStatusResponseModel>, t: Throwable) {
                    progressBarDialog!!.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })

        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }

    private fun callStudentsListAPI() {
        var studentsResponse: StudentsResponseModel
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<StudentsResponseModel> = ApiClient.getClient.getStudents(
                "Bearer "+PreferenceManager.getAccessToken(context) ,"3"
            )
            progressBarDialog!!.show()
            call.enqueue(object : Callback<StudentsResponseModel> {
                override fun onResponse(call: Call<StudentsResponseModel>, response: Response<StudentsResponseModel>) {
                    progressBarDialog!!.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context,getString(R.string.text_unknown_error))
                    } else {
                        studentsResponse = response.body()!!
                        if (studentsResponse.status == 200) {
                            if (studentsResponse.data!!.isNotEmpty()){
                                for (i in studentsResponse.data!!.indices) {
                                    studentArray.add(studentsResponse.data!![i]!!)
                                }
                                totalStudentsTextView.text = studentArray.size.toString()
                                if (studentArray.size > 4){

                                }else if (studentArray.size == 3) {
                                    countTextView.visibility = View.GONE
                                }else if (studentArray.size == 2){
                                    imageC.visibility = View.GONE
                                    countTextView.visibility = View.GONE
                                }else if (studentArray.size == 1){
                                    Glide.with(context)
                                        .load(studentArray[0].profile_photo_path)
                                        .into(imageA)
                                    imageB.visibility = View.GONE
                                    imageC.visibility = View.GONE
                                    countTextView.visibility = View.GONE
                                }else{
                                    Log.e("Wha","dont print this")
                                }
                                Log.e("Student size", studentArray.size.toString())
                            }else{
                                AppUtils.showMessagePopUp(context,"No student data available")
                            }
                        } else if(studentsResponse.status == 401) {
                            AppUtils.showMessagePopUp(context, "Unauthenticated or Token Expired, Please Login")
                        } else {
                            AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                        }
                    }

                }
                override fun onFailure(call: Call<StudentsResponseModel>, t: Throwable) {
                    progressBarDialog!!.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })

        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }
}