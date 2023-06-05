package com.nas.healthandsafety.activity.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.evacuation.StudentEvacuationActivity
import com.nas.healthandsafety.activity.fire_marshall.FireMarshallHomeActivity
import com.nas.healthandsafety.activity.gallery.GalleryActivity
import com.nas.healthandsafety.activity.home.adapter.StudentListAdapter
import com.nas.healthandsafety.activity.home.model.AssemblyPointsResponseModel
import com.nas.healthandsafety.activity.home.model.DeviceRegistrationResponseModel
import com.nas.healthandsafety.activity.home.model.EvacuationStatusResponseModel
import com.nas.healthandsafety.activity.home.model.StudentsResponseModel
import com.nas.healthandsafety.activity.profile.ProfileActivity
import com.nas.healthandsafety.activity.session_select.SessionSelectActivity
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import com.ncorti.slidetoact.SlideToActView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.System.exit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeActivity : AppCompatActivity() {
    //    lateinit var bottomNav: BottomNavigationCircles
    lateinit var context: Context
    lateinit var marshallButton: ImageView
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

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
//                    TODO
//                    signOutCall()
                PreferenceManager.setAccessToken(context, "")
                PreferenceManager.setStaffID(context, "")
                exit(0)
            }
            .setNegativeButton(
                "No"
            ) { dialog, id -> //  Action for 'NO' Button
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    //    var database = Firebase.database
//    var reference = database.reference
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        context = this
        progressBarDialog = ProgressBarDialog(context)
        marshallButton = findViewById(R.id.marshall)
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
        callDeviceRegistrationAPI()
        callGetAssemblyPoints()
        countTextView.setOnClickListener {
            showStudentListPopUp(context, studentArray)
        }
        area.text = PreferenceManager.getAssemblyPoint(context)

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

        if (PreferenceManager.getIsFireMarshall(context)) {
            marshallButton.visibility = View.VISIBLE
        } else {
            marshallButton.visibility = View.GONE
        }
        marshallButton.setOnClickListener {
            val intent = Intent(context, FireMarshallHomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
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

    private fun showStudentListPopUp(
        context: Context,
        studentList: ArrayList<StudentsResponseModel.Data>
    ) {
        val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.dialog_student_list, null)
        val classNameTextView = view.findViewById<TextView>(R.id.className)
        classNameTextView.text = "Class " + PreferenceManager.getClassName(context)
        val studentRecycler = view.findViewById<RecyclerView>(R.id.studentRecycler)
        studentRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = StudentListAdapter(context, studentList)
        studentRecycler.adapter = adapter

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun callGetAssemblyPoints() {
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<AssemblyPointsResponseModel> = ApiClient.getClient.getAssemblyPoints(
                "Bearer " + PreferenceManager.getAccessToken(context),
            )
            progressBarDialog!!.show()
            call.enqueue(object : Callback<AssemblyPointsResponseModel> {
                override fun onResponse(
                    call: Call<AssemblyPointsResponseModel>,
                    response: Response<AssemblyPointsResponseModel>
                ) {
                    progressBarDialog!!.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                    } else {
                        if (response.body()!!.data!!.isNotEmpty()) {

                            for (i in response.body()!!.data!!.indices) {

                                for (j in response.body()!!.data!![i]!!.classes!!.indices) {
                                    val className =
                                        response.body()!!.data!![i]!!.classes!![j]!!.replace(
                                            "\\s+".toRegex(),
                                            ""
                                        )
                                    if (className == PreferenceManager.getClassName(context)) {
                                        Log.e("class", className)
                                        Log.e(
                                            "class2",
                                            response.body()!!.data!![0]!!.assembly_point!!.toString()
                                        )
                                        PreferenceManager.setAssemblyPoint(
                                            context,
                                            response.body()!!.data!![i]!!.assembly_point!!.toString()
                                        )
                                        area.text =
                                            response.body()!!.data!![i]!!.assembly_point!!.toString()
                                    }
                                }
                            }
                        }
//                        deviceRegistrationResponse = response.body()!!
//                        if (deviceRegistrationResponse.status == 200) {
//
//                        } else if(deviceRegistrationResponse.status == 401) {
//                            AppUtils.showMessagePopUp(context, "Unauthenticated or Token Expired, Please Login")
//                        } else {
//                            AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
//                        }
                    }

                }

                override fun onFailure(call: Call<AssemblyPointsResponseModel>, t: Throwable) {
                    progressBarDialog!!.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })

        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }

    private fun callDeviceRegistrationAPI() {
        var deviceRegistrationResponse: DeviceRegistrationResponseModel
        val androidID = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val deviceName = getDeviceName()
        val paramObject = JsonObject().apply {
            addProperty("device_type", "2")
            addProperty("device_id", androidID)
            addProperty("device_name", deviceName)
            addProperty("app_version", "1.0")
            addProperty("fcm_id", PreferenceManager.getFCMID(context))
        }
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<DeviceRegistrationResponseModel> = ApiClient.getClient.postDeviceData(
                "Bearer " + PreferenceManager.getAccessToken(context), paramObject
            )
            progressBarDialog!!.show()
            call.enqueue(object : Callback<DeviceRegistrationResponseModel> {
                override fun onResponse(
                    call: Call<DeviceRegistrationResponseModel>,
                    response: Response<DeviceRegistrationResponseModel>
                ) {
                    progressBarDialog!!.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                    } else {
//                        deviceRegistrationResponse = response.body()!!
//                        if (deviceRegistrationResponse.status == 200) {
//
//                        } else if(deviceRegistrationResponse.status == 401) {
//                            AppUtils.showMessagePopUp(context, "Unauthenticated or Token Expired, Please Login")
//                        } else {
//                            AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
//                        }
                    }

                }

                override fun onFailure(call: Call<DeviceRegistrationResponseModel>, t: Throwable) {
                    progressBarDialog!!.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })

        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }

    private fun callEvacuationStatusAPI() {
        var evacuationStatusResponse: EvacuationStatusResponseModel
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<EvacuationStatusResponseModel> = ApiClient.getClient.getEvacuationStatus(
                "Bearer " + PreferenceManager.getAccessToken(context)
            )
            progressBarDialog!!.show()
            call.enqueue(object : Callback<EvacuationStatusResponseModel> {
                override fun onResponse(
                    call: Call<EvacuationStatusResponseModel>,
                    response: Response<EvacuationStatusResponseModel>
                ) {
                    progressBarDialog!!.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context,getString(R.string.text_unknown_error))
                    } else {
                        evacuationStatusResponse = response.body()!!
                        if (evacuationStatusResponse.status == 200) {
                            if (evacuationStatusResponse.data!!.isNotEmpty()){
                                for (i in evacuationStatusResponse.data!!.indices){
                                    if (evacuationStatusResponse.data!![i]!!.status == 1) {
                                        isEvac = true
                                        Log.e(
                                            "response",
                                            evacuationStatusResponse.data!![i]!!.evacuate_id.toString()
                                        )
                                        firebaseKey =
                                            evacuationStatusResponse.data!![i]!!.evacuate_id!!.toString()
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
                "Bearer " + PreferenceManager.getAccessToken(context),
                PreferenceManager.getClassID(context)
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
                            if (studentsResponse.data!!.isNotEmpty()) {
                                for (i in studentsResponse.data!!.indices) {
                                    studentArray.add(studentsResponse.data!![i]!!)
                                }
//                                if (studentArray.size > 10) {
//                                    totalStudentsTextView.text = "10"
//
//                                } else {
                                Log.e("size", studentArray.size.toString())
                                totalStudentsTextView.text = studentArray.size.toString()

//                                }
                                val count = studentArray.size - 3
                                if (studentArray.size > 4) {
                                    Glide.with(context)
                                        .load(studentArray[0].profile_photo_path)
                                        .into(imageA)
                                    Glide.with(context)
                                        .load(studentArray[1].profile_photo_path)
                                        .into(imageB)
                                    Glide.with(context)
                                        .load(studentArray[2].profile_photo_path)
                                        .into(imageC)
                                    countTextView.visibility = View.VISIBLE
                                    countTextView.text = "+$count"
                                } else if (studentArray.size == 3) {
                                    Glide.with(context)
                                        .load(studentArray[0].profile_photo_path)
                                        .into(imageA)
                                    Glide.with(context)
                                        .load(studentArray[1].profile_photo_path)
                                        .into(imageB)
                                    Glide.with(context)
                                        .load(studentArray[2].profile_photo_path)
                                        .into(imageC)
                                    countTextView.visibility = View.VISIBLE
                                    countTextView.text = "+$count"
                                }else if (studentArray.size == 2) {
                                    Glide.with(context)
                                        .load(studentArray[0].profile_photo_path)
                                        .into(imageA)
                                    Glide.with(context)
                                        .load(studentArray[1].profile_photo_path)
                                        .into(imageB)
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

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer, ignoreCase = true)) {
            model
        } else {
            "$manufacturer $model"
        }
    }
}