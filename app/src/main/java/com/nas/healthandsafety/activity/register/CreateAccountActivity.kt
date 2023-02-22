package com.nas.healthandsafety.activity.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.register.model.RegisterAccountModel
import com.nas.healthandsafety.activity.welcome.WelcomeActivity
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.ProgressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAccountActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var name: EditText
    lateinit var emailID: EditText
    lateinit var mobileNo: EditText
    lateinit var createAccount: Button
    lateinit var backButton: ImageView
    var passwordShowHide1: Boolean = false
    var passwordShowHide2: Boolean = false
    var progressBarDialog: ProgressBarDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        context = this
        name = findViewById(R.id.name)
        emailID = findViewById(R.id.emailID)
        mobileNo = findViewById(R.id.mobileNo)
        createAccount = findViewById(R.id.createAccount)
        backButton = findViewById(R.id.back_button)
        progressBarDialog = ProgressBarDialog(context)
        createAccount.setBackgroundResource(R.drawable.create_account_disabled)
        createAccount.isEnabled = false
        val editTexts = listOf(/*name,*/ emailID/*, mobileNo*/)
        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                    var et1 = name.text.toString().trim()
                    var et2 = emailID.text.toString().trim()
//                    var et3 = mobileNo.text.toString().trim()

                    createAccount.isEnabled = /*et1.isNotEmpty()*/
                           /*&&*/ et2.isNotEmpty()
                            /*&& et3.isNotEmpty()*/
                    if (createAccount.isEnabled) {
                        createAccount.setBackgroundResource(R.drawable.rounded_sign_in)
                    } else {
                        createAccount.setBackgroundResource(R.drawable.create_account_disabled)
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
        createAccount.setOnClickListener {
            if (emailID.text.toString() == "") {
                AppUtils.showMessagePopUp(context, "Field cannot be left empty.")
                createAccount.setBackgroundResource(R.drawable.create_account_disabled)
            } else {
                createAccount.setBackgroundResource(R.drawable.rounded_sign_in)
                val emailPattern = AppUtils.isEmailValid(emailID.text.toString())
                if (!emailPattern) {
                    AppUtils.showMessagePopUp(context, "Enter a Valid Email.")
                    createAccount.setBackgroundResource(R.drawable.create_account_disabled)
                } else {
                    if (AppUtils.isInternetAvailable(context)) {
                        callCreateAccountApi()
                    } else {
                        AppUtils.showMessagePopUp(
                            context,
                            "Network error occurred. Please check your internet connection and try again later."
                        )
                        createAccount.setBackgroundResource(R.drawable.create_account_disabled)
                    }

                }
            }
//            if (name.text.toString().equals("") || emailID.text.toString()
//                    .equals("") || mobileNo.text.toString().equals("")
//            ) {
//                AppUtils.showMessageOKPopUp(context, "Field cannot be left empty.")
//                createAccount.setBackgroundResource(R.drawable.create_account_disabled)
//            } else {
//                createAccount.setBackgroundResource(R.drawable.rounded_sign_in)
//                var emailPattern = AppUtils.isEmailValid(emailID.text.toString())
//                if (!emailPattern) {
//                    AppUtils.showMessageOKPopUp(context, "Enter a Valid Email.")
//                    createAccount.setBackgroundResource(R.drawable.create_account_disabled)
//                } else {
//                    if (AppUtils.isInternetAvailable(context)) {
//                        callCreateAccountApi()
//                    } else {
//                        AppUtils.showMessageOKPopUp(
//                            context,
//                            "Network error occurred. Please check your internet connection and try again later."
//                        )
//                        createAccount.setBackgroundResource(R.drawable.create_account_disabled)
//                    }
//
//                }
//            }
        }
        backButton.setOnClickListener {
            val intent = Intent(context, WelcomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

    }
    private fun callCreateAccountApi() {
        val androidID = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        var createAccountResponse: RegisterAccountModel
        val call: Call<RegisterAccountModel> = ApiClient.getClient.register(
            emailID.text.toString(),
        )
        progressBarDialog!!.show()
        if (AppUtils.isInternetAvailable(context)) {
            call.enqueue(object : Callback<RegisterAccountModel> {
                override fun onResponse(
                    call: Call<RegisterAccountModel>,
                    response: Response<RegisterAccountModel>
                ) {
                    progressBarDialog!!.hide()
                    if (!response.body()!!.equals("")) {
                        AppUtils.showMessagePopUp(context, response.body()!!.message.toString())

//                        createAccountResponse = response.body()!!
//                        if (createAccountResponse.status.toString() == "121") {
//                            AppUtils.showMessageOKPopUp(context, "Already Registered")
//                            val intent = Intent(context, SignInActivity::class.java)
//                            startActivity(intent)
//                            overridePendingTransition(0, 0)
//                            finish()
//                        } else if (createAccountResponse.status.toString() == "114") {
//                            AppUtils.showMessageOKPopUp(context, "Invalid User")
//                        } else if (createAccountResponse.status.toString() == "402") {
//                            AppUtils.showMessageOKPopUp(context, "Some Error Occurred")
////                            AppUtils.getAccessTokenAPICall(context)
//                        }
                    }
                }

                override fun onFailure(call: Call<RegisterAccountModel>, t: Throwable) {
                    progressBarDialog!!.hide()
                    AppUtils.showMessagePopUp(context, getString(R.string.text_network_error))
//                    AppUtils.getAccessTokenAPICall(context)
                }

            })
        } else{
            AppUtils.showNetworkErrorPopUp(context)
        }
    }

}