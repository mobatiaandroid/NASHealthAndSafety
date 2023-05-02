package com.nas.healthandsafety.activity.session_select

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.fire_marshall.model.CommonResponseModel
import com.nas.healthandsafety.activity.home.HomeActivity
import com.nas.healthandsafety.activity.login.SignInActivity
import com.nas.healthandsafety.activity.session_select.model.SubjectsResponseModel
import com.nas.healthandsafety.activity.session_select.model.YearGroupsResponseModel
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionSelectActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var progressBarDialog: ProgressBarDialog
    var classID = "0"
    var yearGroupsArray: ArrayList<YearGroupsResponseModel.Data> = ArrayList()
    var yearGroupsList: ArrayList<String> = ArrayList()
    var subjectsArray: ArrayList<SubjectsResponseModel.Data> = ArrayList()
    var subjectsList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_select)
        context = this
//        var yearGroupsArrayList: ArrayList<Lists> = ArrayList()

        progressBarDialog = ProgressBarDialog(context)
        showSelectSessionPopUp()
    }


    private fun showSelectSessionPopUp() {
        var yearGroupsResponse: YearGroupsResponseModel

        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<YearGroupsResponseModel> = ApiClient.getClient.getYearGroups(
                "Bearer "+PreferenceManager.getAccessToken(context)
            )
            progressBarDialog.show()
            call.enqueue(object : Callback<YearGroupsResponseModel> {
                override fun onResponse(call: Call<YearGroupsResponseModel>, response: Response<YearGroupsResponseModel>) {
                    progressBarDialog.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context,getString(R.string.text_unknown_error))
                    } else {
                        yearGroupsResponse = response.body()!!
                        if (yearGroupsResponse.status == 200) {
                            if (yearGroupsResponse.message.equals("success", ignoreCase = true)) {
                                if (yearGroupsResponse.data!!.size >0) {
                                    for (i in  yearGroupsResponse.data!!.indices){
                                        yearGroupsArray.add(yearGroupsResponse.data!![i]!!)
                                    }
                                    for (i in yearGroupsArray.indices){
                                        yearGroupsList.add(yearGroupsArray[i].year_group.toString())
                                    }

                                }

                            }
                        } else if(yearGroupsResponse.status == 401) {
                            AppUtils.showMessagePopUp(context, "Unauthenticated or Token Expired, Please Login")
                        } else {
                            AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                        }
                    }

                }
                override fun onFailure(call: Call<YearGroupsResponseModel>, t: Throwable) {
                    progressBarDialog.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_session_select)
            dialog.setCancelable(false)
            var sessionSelect = dialog.findViewById<View>(R.id.sessionSelect)
            var subjectSelect = dialog.findViewById<View>(R.id.subjectSelect)
            var selectedSession = dialog.findViewById<View>(R.id.selectedSession) as TextView
            var selectedSubject = dialog.findViewById<View>(R.id.selectedSubject) as TextView
            var closeButton = dialog.findViewById<View>(R.id.close)
            var checkInButton = dialog.findViewById<View>(R.id.checkIn)
            var position = -1
            var position2 = -1
            dialog.show()
            subjectSelect.setOnClickListener {
                if (selectedSession.text == ""){
                    AppUtils.showMessagePopUp(context, getString(R.string.text_select_session))
                }else{
                    if (subjectsList.isNotEmpty()){
                        val subjectSelector: Array<String> = subjectsList.toTypedArray()
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Select Subject")
                        var checkedItem = -1
                        builder.setSingleChoiceItems(subjectSelector, checkedItem) { dialog, which ->
                            checkedItem = which
                        }
                        builder.setPositiveButton("OK") { dialog, which ->
                            if (checkedItem == -1){
                                AppUtils.showMessagePopUp(context, getString(R.string.text_select))
                            }
                            else{
                                selectedSubject.text = subjectSelector[checkedItem]
                                PreferenceManager.setSubject(context, subjectSelector[checkedItem].toString())
                                position2 = checkedItem
                            }

                        }
                        builder.setNegativeButton("Cancel", null)
                        val dialog = builder.create()
                        dialog.show()
                    } else{
                        AppUtils.showMessagePopUp(context,"No subjects available")
                    }

                }
//
            }
            sessionSelect.setOnClickListener {
                var yearGroupsSelector: Array<String> = yearGroupsList.toTypedArray()
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Session")
                var checkedItem = -1
                builder.setSingleChoiceItems(yearGroupsSelector, checkedItem) { dialog, which ->
                    checkedItem = which
                }
                builder.setPositiveButton("OK") { dialog, which ->
                    if (checkedItem == -1){
                        AppUtils.showMessagePopUp(context, getString(R.string.text_select))
                    }
                    else{
                        selectedSession.text = yearGroupsSelector[checkedItem]
                        position = checkedItem
                        PreferenceManager.setClassName(context, yearGroupsSelector[checkedItem].toString())
                        classID = yearGroupsArray[position].id.toString()
                        PreferenceManager.setClassID(context, classID)
                        callSubjectListAPI()
                    }
//                Log.e("ClassIDSEssionSelecet", yearGroupsArrayList[checkedItem].id)
                }
                builder.setNegativeButton("Cancel", null)
                val dialog = builder.create()
                dialog.show()
            }
            closeButton.setOnClickListener {
                val intent = Intent(context, SignInActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
            }
            checkInButton.setOnClickListener {
                if (selectedSession.text.equals("")) {
                    AppUtils.showMessagePopUp(context, getString(R.string.text_select_session))
                } else if (selectedSubject.text.equals("")) {
                    AppUtils.showMessagePopUp(context, getString(R.string.text_select_subject))
                } else {
//                    intent into home & save value in
                    callCheckInAPI()


                }
            }
            val editTexts = listOf(selectedSession,selectedSubject)
            for (editText in editTexts) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        var et1 = selectedSession.text.toString().trim()
                        var et2 = selectedSubject.text.toString().trim()

                        checkInButton.isEnabled = et1.isNotEmpty()
                                && et2.isNotEmpty()
                        if (checkInButton.isEnabled) {
                            checkInButton.setBackgroundResource(R.drawable.rounded_rectangle_green)
                        } else {
                            checkInButton.setBackgroundResource(R.drawable.rounded_rectangle_green_disabled)
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int, count: Int, after: Int) {
                    }

                    override fun afterTextChanged(
                        s: Editable
                    ) {
                    }
                })
            }
        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }

    }

    private fun callCheckInAPI() {
        val paramObject = JsonObject().apply {
            addProperty("latitude", "8.557420")
            addProperty("longitude", "76.853180")

        }
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<CommonResponseModel> = ApiClient.getClient.postStaffAttendance(
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
                        AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                    } else {
                        val intent = Intent(context, HomeActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
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

    private fun callSubjectListAPI() {
        var subjectsResponse: SubjectsResponseModel
        if (AppUtils.isInternetAvailable(context)) {
            val call: Call<SubjectsResponseModel> = ApiClient.getClient.getSubjects(
                "Bearer " + PreferenceManager.getAccessToken(context), classID
            )
            progressBarDialog.show()
            call.enqueue(object : Callback<SubjectsResponseModel> {
                override fun onResponse(
                    call: Call<SubjectsResponseModel>,
                    response: Response<SubjectsResponseModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() == null) {
                        AppUtils.showMessagePopUp(context,getString(R.string.text_unknown_error))
                    } else {
                        Log.e("subjects", response.body().toString())
                        subjectsResponse = response.body()!!
                        if (subjectsResponse.status == 200) {
                            if (subjectsResponse.message.equals("success", ignoreCase = true)) {
                                if (subjectsResponse.data!!.isNotEmpty()) {
                                    for (i in  subjectsResponse.data!!.indices){
                                        subjectsArray.add(subjectsResponse.data!![i]!!)
                                    }
                                    for (i in subjectsArray.indices){
                                        subjectsList.add(subjectsArray[i].name.toString())
                                    }
                                    Log.e("subjects size", subjectsList.size.toString())

                                }

                            }
                        } else if(subjectsResponse.status == 401) {
                            AppUtils.showMessagePopUp(context, "Unauthenticated or Token Expired, Please Login")
                        } else {
                            AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                        }
                    }

                }
                override fun onFailure(call: Call<SubjectsResponseModel>, t: Throwable) {
                    progressBarDialog.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                }

            })
        } else {
            AppUtils.showNetworkErrorPopUp(context)
        }
    }

}