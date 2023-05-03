package com.nas.healthandsafety.activity.fire_marshall

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.lzyzsd.circleprogress.ArcProgress
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.JsonObject
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.evacuation.model.StudentModel
import com.nas.healthandsafety.activity.fire_marshall.model.CommonResponseModel
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarshallEvacuationActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var arcProgress: ArcProgress
    lateinit var evacuationEndButton: ExtendedFloatingActionButton
    lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var database: DatabaseReference
    lateinit var studentArray: ArrayList<StudentModel>
    var maxCount = 1
    var count = 0
    private lateinit var handler: Handler
    private var progressValue = 0
    override fun onBackPressed() {
        val intent = Intent(context, FireMarshallHomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marshall_evacuation)
        context = this
        progressBarDialog = ProgressBarDialog(context)
        arcProgress = findViewById(R.id.arcProgress)
        evacuationEndButton = findViewById(R.id.floatingActionButton)
        evacuationEndButton.setOnClickListener {
            callEndEvacuationAPI()
        }
        handler = Handler(Looper.getMainLooper())

        // Start the progress update
//        startProgressUpdate()

        database = FirebaseDatabase.getInstance().reference
        database = FirebaseDatabase.getInstance().getReference("evacuation_students")
            .child(PreferenceManager.getFireRef(context))
        count = 0
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    count = 0
                    maxCount = snapshot.childrenCount.toInt()
                    if (maxCount > 10) {
                        maxCount = 10
                    }
                    arcProgress.max = maxCount
                    for (i in snapshot.children) {
                        if (i.child("evacuated").value.toString() == "1") {
                            count += 1
                            Log.e("COunt", count.toString())
                        }
                    }
                    arcProgress.progress = count
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("err", "error")
            }

        })

    }

    private fun startProgressUpdate() {
        handler.postDelayed({
            // Increase the progress value
            progressValue += 1
            arcProgress.progress = progressValue
            arcProgress.animate()

            if (progressValue < arcProgress.max) {
                // Call the method again after a delay
                startProgressUpdate()
            }
        }, 1000)
    }

    private fun callEndEvacuationAPI() {
        val paramObject = JsonObject().apply {
            addProperty("evacuate_id", PreferenceManager.getFireRef(context))

        }
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<CommonResponseModel> = ApiClient.getClient.postEvacuationEnd(
                "Bearer " + PreferenceManager.getAccessToken(context), paramObject
            )
            progressBarDialog.show()
            call.enqueue(object : Callback<CommonResponseModel> {
                override fun onResponse(
                    call: Call<CommonResponseModel>,
                    response: Response<CommonResponseModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() == null) {

                    } else {
                        showPopUp(context, "Evacuation Completed")
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

    private fun showPopUp(context: Context, message: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_alert_ok)
        val text = dialog.findViewById<android.view.View>(R.id.textDialog) as TextView
        val button = dialog.findViewById<android.view.View>(R.id.okButton) as Button
        button.setOnClickListener {
            val intent = Intent(context, FireMarshallHomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        text.text = message
        dialog.show()
    }

}