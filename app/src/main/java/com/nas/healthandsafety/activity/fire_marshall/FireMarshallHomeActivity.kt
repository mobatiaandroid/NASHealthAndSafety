package com.nas.healthandsafety.activity.fire_marshall

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.attendance.AttendanceActivity
import com.nas.healthandsafety.activity.gallery.GalleryActivity
import com.nas.healthandsafety.activity.profile.ProfileActivity

class FireMarshallHomeActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var attendenceButton: ImageView
    lateinit var myProfile: ImageView
    lateinit var gallery: ImageView

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
        evacuateButton = findViewById(R.id.evacuateButton)
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
            showSelectEmergencyPopUp()
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