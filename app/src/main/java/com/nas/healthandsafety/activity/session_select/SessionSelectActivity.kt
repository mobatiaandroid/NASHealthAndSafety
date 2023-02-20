package com.nas.healthandsafety.activity.session_select

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.TextView
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.home.HomeActivity
import com.nas.healthandsafety.activity.login.SignInActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_select)
        context = this
        progressBarDialog = ProgressBarDialog(context)
        showSelectSessionPopUp()
    }


    private fun showSelectSessionPopUp() {
        var yearGroupsResponse: YearGroupsResponseModel
//        var yearGroupsArrayList: ArrayList<Lists> = ArrayList()
        var subjectArrayList: ArrayList<String> = ArrayList()
        var i: Int = 0
        var yearGroupsList: ArrayList<String> = ArrayList()
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
                                // TODO
//                                    yearGroupsArrayList =
//                                        yearGroupsResponse.response.data.lists as ArrayList<Lists>
//                                    while (i < yearGroupsArrayList.size) {
//                                        yearGroupsList.add(yearGroupsArrayList[i].year_group)
//                                        i++
//                                    }
                                yearGroupsList.add("I")
                                yearGroupsList.add("II")
                                yearGroupsList.add("III")
                                yearGroupsList.add("IV")
                                yearGroupsList.add("V")
                                subjectArrayList.add("English")
                                subjectArrayList.add("Mathematics")
                                subjectArrayList.add("Science")
                                subjectArrayList.add("Social Science")
                                subjectArrayList.add("Arabic")
                                subjectArrayList.add("Computer Science")

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
            val dialog = Dialog(context!!)
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
                var subjectSelector: Array<String> = subjectArrayList.toTypedArray()
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
                        position2 = checkedItem
                    }

                }
                builder.setNegativeButton("Cancel", null)
                val dialog = builder.create()
                dialog.show()
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
//                    TODO
//                    intent into home & save value in pref
                    val intent = Intent(context, HomeActivity::class.java)
//                    PreferenceManager.setClassID(context, yearGroupsArrayList[position].id)
//                    PreferenceManager.setClassName(context, selectedSession.text.toString())
//                    PreferenceManager.setSubject(context, selectedSubject.text.toString())
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()

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

}