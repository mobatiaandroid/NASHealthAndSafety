package com.nas.healthandsafety.activity.fire_marshall

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.attendance.AttendanceActivity
import com.nas.healthandsafety.activity.fire_marshall.model.EvacuationStartResponseModel
import com.nas.healthandsafety.activity.gallery.GalleryActivity
import com.nas.healthandsafety.activity.profile.ProfileActivity
import com.nas.healthandsafety.activity.report.ReportActivity
import com.nas.healthandsafety.activity.session_select.model.YearGroupsResponseModel
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FireMarshallHomeActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var attendenceButton: ImageView
    lateinit var myProfile: ImageView
    lateinit var gallery: ImageView
    lateinit var reports: Button
    lateinit var staffNameTextView: TextView
    lateinit var progressBarDialog: ProgressBarDialog
    //    lateinit var extras: Bundle
//    lateinit var classID: String
//    lateinit var staffName: TextView
//    lateinit var imageA: ImageView
//    lateinit var imageB: ImageView
//    lateinit var imageC: ImageView
//    lateinit var count: TextView
//    lateinit var greeting: TextView
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_marshall_home)
        context = this
//        progressBarDialog = ProgressBarDialog(context)
        attendenceButton = findViewById(R.id.attendence)
        gallery = findViewById(R.id.gallery)
        myProfile = findViewById(R.id.myProfile)
        reports = findViewById(R.id.button) as Button
        staffNameTextView = findViewById(R.id.staffName)
        progressBarDialog = ProgressBarDialog(context)
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
        attendenceButton.setOnClickListener {
            val intent = Intent(context, AttendanceActivity::class.java)
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
            evacuationStartAPICall()
        }
    }

    private fun evacuationStartAPICall() {
        var evacuationStartResponse: EvacuationStartResponseModel
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<EvacuationStartResponseModel> = ApiClient.getClient.postEvacuationStart(
                "Bearer "+PreferenceManager.getAccessToken(context)
            )
            progressBarDialog!!.show()
            call.enqueue(object : Callback<EvacuationStartResponseModel> {
                override fun onResponse(call: Call<EvacuationStartResponseModel>, response: Response<EvacuationStartResponseModel>) {
                    progressBarDialog!!.hide()
                    if (response.body() == null) {
                        if(response.code() == 404) {
                            AppUtils.showMessagePopUp(context, "An evacuation is already in progress.")
                        }
                    } else {
                        evacuationStartResponse = response.body()!!
                        if (evacuationStartResponse.status == 200) {
                            AppUtils.showMessagePopUp(context,"Evacuation Started")
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
                    progressBarDialog!!.hide()
                    Log.e("message",t.message.toString()+t.localizedMessage.toString()+ t.cause!!.localizedMessage.toString())
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
//        val text = dialog.findViewById<View>(R.id.textDialog) as TextView
//        text.text = context.getString(R.string.text_network_error)
        dialog.show()
    }
}