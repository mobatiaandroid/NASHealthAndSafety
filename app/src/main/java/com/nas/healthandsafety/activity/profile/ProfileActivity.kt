package com.nas.healthandsafety.activity.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.attendance.AttendanceActivity
import com.nas.healthandsafety.activity.gallery.GalleryActivity
import com.nas.healthandsafety.activity.home.HomeActivity
import com.nas.healthandsafety.activity.welcome.WelcomeActivity
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog

class ProfileActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var homeButton: ImageView
    lateinit var attendanceButton: ImageView
    lateinit var gallery: ImageView
    lateinit var notifications: TextView
    lateinit var checkout: TextView
    lateinit var settings: TextView
    lateinit var changePassword: TextView
    lateinit var editButton: ImageView
    lateinit var staffName: EditText
    lateinit var staffDesignation: EditText
    var progressBarDialog: ProgressBarDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        context = this
        homeButton = findViewById(R.id.home)
        attendanceButton = findViewById(R.id.attendence)
        gallery = findViewById(R.id.gallery)
        notifications = findViewById(R.id.notifications)
        settings = findViewById(R.id.settings)
        checkout = findViewById(R.id.checkout)
        editButton = findViewById(R.id.edit)
        staffName = findViewById(R.id.staffName)
        staffDesignation = findViewById(R.id.staffDesignation)
        changePassword = findViewById(R.id.changePassword)
        progressBarDialog = ProgressBarDialog(context)
        staffName.setOnKeyListener(null)
        staffDesignation.setOnKeyListener(null)
        staffName.text =
            Editable.Factory.getInstance().newEditable(PreferenceManager.getStaffName(context))
        var click = 1
        editButton.setOnClickListener {
            if (click == 1) {
                staffName.isEnabled = true
                staffDesignation.isEnabled = true
                editButton.setImageResource(R.drawable.tick_icon)
                staffName.text.clear()
                staffName.hint = PreferenceManager.getStaffName(context)
                staffDesignation.text.clear()
                staffDesignation.hint = "English Lecturer"
                click = 2
            } else if (click == 2) {
                editButton.setImageResource(R.drawable.edit_icon)
                click = 1
            }

        }
        gallery.setOnClickListener {
            val intent = Intent(context, GalleryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        changePassword.setOnClickListener {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.change_password_popup)

            val currentPassword = dialog.findViewById<View>(R.id.currentPassword) as EditText
            val newPassword = dialog.findViewById<View>(R.id.newPassword) as EditText
            val confirmPassword = dialog.findViewById<View>(R.id.confirmPassword) as EditText
            val submit = dialog.findViewById<View>(R.id.submit)
            submit.isEnabled = false
            val editTexts = listOf(currentPassword, newPassword, confirmPassword)
            for (editText in editTexts) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                        var et1 = currentPassword.text.toString().trim()
                        var et2 = newPassword.text.toString().trim()
                        var et3 = confirmPassword.text.toString().trim()

                        submit.isEnabled = et1.isNotEmpty()
                                && et2.isNotEmpty()
                                && et3.isNotEmpty()
                        if (submit.isEnabled) {
                            submit.setBackgroundResource(R.drawable.rounded_sign_in)
                        } else {
                            submit.setBackgroundResource(R.drawable.create_account_disabled)
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int, count: Int, after: Int,
                    ) {
                    }

                    override fun afterTextChanged(
                        s: Editable,
                    ) {
                    }
                })
            }
            submit.setOnClickListener {
                if (currentPassword.text.toString() == "" || newPassword.text.toString() == "" || confirmPassword.text.toString() == ""
                ) {
                    AppUtils.showMessagePopUp(context, "Field cannot be empty.")
                } else if (newPassword.text.toString() != confirmPassword.text.toString()) {
                    AppUtils.showMessagePopUp(context, "Passwords do not match")
                } else {
//                    TODO
//                    changePasswordAPICall(currentPassword.text.toString(),newPassword.text.toString())
                }
            }

            dialog.show()
        }

        homeButton.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        attendanceButton.setOnClickListener {
            val intent = Intent(context, AttendanceActivity::class.java)
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
        checkout.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
//                    TODO
//                    signOutCall()
                    val intent = Intent(context, WelcomeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id -> //  Action for 'NO' Button
                    dialog.cancel()
                }
            val alert: AlertDialog = builder.create()
            alert.show()

        }
    }
}