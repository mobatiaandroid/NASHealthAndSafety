package com.nas.healthandsafety.activity.fire_marshall

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.JsonObject
import com.nas.healthandsafety.MainActivity
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.fire_marshall.model.CommonResponseModel
import com.nas.healthandsafety.activity.fire_marshall.model.EvacuationStartResponseModel
import com.nas.healthandsafety.activity.gallery.GalleryActivity
import com.nas.healthandsafety.activity.home.HomeActivity
import com.nas.healthandsafety.activity.home.model.DeviceRegistrationResponseModel
import com.nas.healthandsafety.activity.home.model.EvacuationStatusResponseModel
import com.nas.healthandsafety.activity.profile.ProfileActivity
import com.nas.healthandsafety.activity.report.ReportActivity
import com.nas.healthandsafety.activity.session_select.SessionSelectActivity
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import com.ncorti.slidetoact.SlideToActView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date


class FireMarshallHomeActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var marshallButton: ImageView
    lateinit var homeButton: ImageView
    lateinit var myProfile: ImageView
    lateinit var gallery: ImageView
    lateinit var reports: Button
    lateinit var staffNameTextView: TextView
    lateinit var pastEvacuationsButton: Button
    lateinit var progressBarDialog: ProgressBarDialog
    lateinit var slider: SlideToActView
    lateinit var button1: Button
    lateinit var button2: Button

    var isEvac = false
    var firebaseKey = ""

    //    lateinit var extras: Bundle
//    lateinit var classID: String
//    lateinit var staffName: TextView
//    lateinit var imageA: ImageView
//    lateinit var imageB: ImageView
//    lateinit var imageC: ImageView
//    lateinit var count: TextView
    lateinit var greeting: TextView

    //    lateinit var totalStudents: TextView
//    lateinit var presentStudents: TextView
//    lateinit var absentStudents: TextView
//    lateinit var subject: TextView
//    lateinit var progressBarPresent: ProgressBar
//    lateinit var progressBarAbsent: ProgressBar
//    lateinit var className: TextView
//    lateinit var date: TextView
//    lateinit var assemblyAreaSelector: View
//    lateinit var area: TextView
//    lateinit var slider: SlideToActView
    lateinit var evacuateButton: ExtendedFloatingActionButton
    override fun onBackPressed() {
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_marshall_home)
        context = this
//        progressBarDialog = ProgressBarDialog(context)
        marshallButton = findViewById(R.id.attendence)
        homeButton = findViewById(R.id.imageView12)
        gallery = findViewById(R.id.gallery)
        myProfile = findViewById(R.id.myProfile)
        reports = findViewById<Button>(R.id.button)
        staffNameTextView = findViewById(R.id.staffName)
        progressBarDialog = ProgressBarDialog(context)
        pastEvacuationsButton = findViewById(R.id.pastEvacuationsButton)
        slider = findViewById(R.id.slider)
        button1 = findViewById(R.id.button23)
        button2 = findViewById(R.id.button2)
        greeting = findViewById(R.id.greeting)

        greetingSetter()
        callDeviceRegistrationAPI()
        callEvacuationStatusAPI()


        button1.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        button2.setOnClickListener {
            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
        slider.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                val intent = Intent(context, SessionSelectActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }
        }

//        staffName = findViewById(R.id.staffName)
//        imageA = findViewById(R.id.imageA)
//        imageB = findViewById(R.id.imageB)
//        imageC = findViewById(R.id.imageC)
//        count = findViewById(R.id.count)
//        totalStudents = findViewById(R.id.totalStudents)
//        presentStudents = findViewById(R.id.presentStudents)
//        absentStudents = findViewById(R.id.absentStudents)
//        progressBarPresent = findViewById(R.id.progressBarPresent)
//        progressBarAbsent = findViewById(R.id.progressBarAbsent)
//        className = findViewById(R.id.className)
//        subject = findViewById(R.id.subjectName)
//        greeting = findViewById(R.id.greeting)
//        date = findViewById(R.id.date)
//        assemblyAreaSelector = findViewById(R.id.assemblyAreaSelector)
//        area = findViewById(R.id.area)
//        slider = findViewById(R.id.slider)
        staffNameTextView.text = PreferenceManager.getStaffName(context)
        evacuateButton = findViewById(R.id.evacuateButton)
        homeButton.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
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
        reports.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
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
        marshallButton.setOnClickListener {


            val intent = Intent(context, FireMarshallHomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        pastEvacuationsButton.setOnClickListener {
            val intent = Intent(context, PastEvacuationsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

//        slider.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
//            override fun onSlideComplete(view: SlideToActView) {
//                val intent = Intent(context, SessionSelectActivity::class.java)
//                startActivity(intent)
//                overridePendingTransition(0, 0)
//                finish()
//            }
//        }
        evacuateButton.setOnClickListener {
//            showSelectEmergencyPopUp()
            notifyStaffAPICall()
        }
    }

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

    private fun notifyStaffAPICall() {
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<CommonResponseModel> = ApiClient.getClient.postNotificationStaff(
                "Bearer " + PreferenceManager.getAccessToken(context)
            )
            progressBarDialog.show()
            call.enqueue(object : Callback<CommonResponseModel> {
                override fun onResponse(
                    call: Call<CommonResponseModel>,
                    response: Response<CommonResponseModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                    } else {
                        if (response.body()!!.status == 200) {
                            Log.e("Here", response.body()!!.data.toString())
                            showSelectEmergencyPopUp()
                        } else {
                            AppUtils.showMessagePopUp(context, "Staff Notification not sent!")
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

                override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                    progressBarDialog.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })

        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }

    private fun evacuationStartAPICall(type: String) {
        var evacuationStartResponse: EvacuationStartResponseModel
        val paramObject = JsonObject().apply {
            addProperty("evacuate_type", type)

        }
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<EvacuationStartResponseModel> = ApiClient.getClient.postEvacuationStart(
                "Bearer " + PreferenceManager.getAccessToken(context),
                paramObject
            )
            progressBarDialog.show()
            call.enqueue(object : Callback<EvacuationStartResponseModel> {
                override fun onResponse(
                    call: Call<EvacuationStartResponseModel>,
                    response: Response<EvacuationStartResponseModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() == null) {
                        if (response.code() == 404) {
                            showPopUp(context, "An evacuation is already in progress.", true)
                        }
                    } else {
                        evacuationStartResponse = response.body()!!
                        if (evacuationStartResponse.status == 200) {
                            val firebaseKey = evacuationStartResponse.data!!.get_key.toString()
                            PreferenceManager.setFireRef(context, firebaseKey)
                            showPopUp(context, "An evacuation has been started. ", false)
                        } else if(evacuationStartResponse.status == 401) {
                            AppUtils.showMessagePopUp(context, "Unauthenticated or Token Expired, Please Login")
                        } else if(evacuationStartResponse.status == 404) {
                            AppUtils.showMessagePopUp(context, "An evacuation is already in progress.")
                        }else {
                            AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                        }
                    }

                }
                override fun onFailure(call: Call<EvacuationStartResponseModel>, t: Throwable) {
                    progressBarDialog.hide()
                    Log.e(
                        "message",
                        t.message.toString() + t.localizedMessage.toString() + t.cause!!.localizedMessage.toString()
                    )
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })

        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }

    private fun showSelectEmergencyPopUp() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_emergency_select)
        val slider = dialog.findViewById<SlideToActView>(R.id.slider)
        val slider2 = dialog.findViewById<SlideToActView>(R.id.slider2)
        val slider3 = dialog.findViewById<SlideToActView>(R.id.slider3)
        slider.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                evacuationStartAPICall("Fire Emergency")
            }
        }
        slider2.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                evacuationStartAPICall("Bomb Alert")
            }
        }
        slider3.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                evacuationStartAPICall("Emergency Drill")
            }
        }
//        val text = dialog.findViewById<View>(R.id.textDialog) as TextView
//        text.text = context.getString(R.string.text_network_error)
        dialog.show()
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
            progressBarDialog.show()
            call.enqueue(object : Callback<DeviceRegistrationResponseModel> {
                override fun onResponse(
                    call: Call<DeviceRegistrationResponseModel>,
                    response: Response<DeviceRegistrationResponseModel>
                ) {
                    progressBarDialog.hide()
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
                    progressBarDialog.hide()
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

    private fun showPopUp(context: Context, message: String, type: Boolean) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_alert_ok)
        val text = dialog.findViewById<android.view.View>(R.id.textDialog) as TextView
        val button = dialog.findViewById<android.view.View>(R.id.okButton) as Button
        button.setOnClickListener {
            val intent = Intent(context, MarshallEvacuationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        text.text = message
        dialog.show()

    }

    private fun callEvacuationStatusAPI() {
        var evacuationStatusResponse: EvacuationStatusResponseModel
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<EvacuationStatusResponseModel> = ApiClient.getClient.getEvacuationStatus(
                "Bearer " + PreferenceManager.getAccessToken(context)
            )
            progressBarDialog.show()
            call.enqueue(object : Callback<EvacuationStatusResponseModel> {
                override fun onResponse(
                    call: Call<EvacuationStatusResponseModel>,
                    response: Response<EvacuationStatusResponseModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                    } else {
                        evacuationStatusResponse = response.body()!!
                        if (evacuationStatusResponse.status == 200) {
                            if (evacuationStatusResponse.data!!.isNotEmpty()) {
                                for (i in evacuationStatusResponse.data!!.indices) {
                                    if (evacuationStatusResponse.data!![i]!!.status == 1) {
                                        isEvac = true
                                        Log.e(
                                            "tesponse",
                                            evacuationStatusResponse.data!![i]!!.evacuate_id.toString()
                                        )
                                        firebaseKey =
                                            evacuationStatusResponse.data!![i]!!.evacuate_id!!.toString()
                                        PreferenceManager.setFireRef(context, firebaseKey)
                                    }
                                }
                            } else {
                                AppUtils.showMessagePopUp(context, "No evacuations are in progress")
                            }
                        } else if (evacuationStatusResponse.status == 401) {
                            AppUtils.showMessagePopUp(
                                context,
                                "Unauthenticated or Token Expired, Please Login"
                            )
                        } else {
                            AppUtils.showMessagePopUp(
                                context,
                                getString(R.string.text_unknown_error)
                            )
                        }
                    }

                }

                override fun onFailure(call: Call<EvacuationStatusResponseModel>, t: Throwable) {
                    progressBarDialog.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })

        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }


}