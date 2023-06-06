package com.nas.healthandsafety.activity.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.fire_marshall.FireMarshallHomeActivity
import com.nas.healthandsafety.activity.login.model.LoginResponseModel
import com.nas.healthandsafety.activity.register.CreateAccountActivity
import com.nas.healthandsafety.activity.session_select.SessionSelectActivity
import com.nas.healthandsafety.activity.welcome.WelcomeActivity
import com.nas.healthandsafety.constants.ApiClient
import com.nas.healthandsafety.constants.AppUtils
import com.nas.healthandsafety.constants.PreferenceManager
import com.nas.healthandsafety.constants.ProgressBarDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    lateinit var backButton: ImageView
    lateinit var emailID: EditText
    lateinit var password: EditText
    lateinit var context: Context
    lateinit var signIn: TextView
    lateinit var createAccount: TextView
    lateinit var showHide: TextView
    lateinit var staffName: String
    lateinit var staffID: String
    lateinit var recoverAccount: TextView
    lateinit var isMarshal: String
    var progressBarDialog: ProgressBarDialog? = null
    var passwordShowHide:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        context = this
        backButton = findViewById(R.id.back_button)
        emailID = findViewById(R.id.emailID)
        password = findViewById(R.id.password)
        signIn = findViewById(R.id.signIn)
        createAccount = findViewById(R.id.createAccount)
        signIn.isEnabled = false
        showHide = findViewById(R.id.showHide)
        progressBarDialog = ProgressBarDialog(context)
        recoverAccount = findViewById(R.id.recoverAccount)
        val editTexts = listOf(emailID,password)
        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    var et1 = password.text.toString().trim()
                    var et2 = emailID.text.toString().trim()
                    signIn.isEnabled = et1.isNotEmpty()
                            && et2.isNotEmpty()
                    if (signIn.isEnabled) {
                        signIn.setBackgroundResource(R.drawable.rounded_sign_in)
                    } else {
                        signIn.setBackgroundResource(R.drawable.create_account_disabled)
                    }

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
        recoverAccount.setOnClickListener {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_recover_account)
            val emailID = dialog.findViewById<View>(R.id.emailID) as EditText
            val submit = dialog.findViewById<View>(R.id.submit)
            submit.setOnClickListener {
                if (emailID.text.toString().equals("")) {
                    AppUtils.showMessagePopUp(context, "Field cannot be empty.")
                } else {
                    val emailPattern = AppUtils.isEmailValid(emailID.text.toString())
                    if (!emailPattern) {
                        AppUtils.showMessagePopUp(context, "Enter a Valid Email.")
                    } else {
                        // call API
                        Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
//                        TODO
//                        callAccountRecoveryAPI(emailID.text.toString())
                    }
                }
            }
            dialog.show()
        }
        backButton.setOnClickListener {
            val intent = Intent(context, WelcomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        }
        signIn.setOnClickListener {
            if (emailID.text.toString().equals("")) {
                AppUtils.showMessagePopUp(context, "Field cannot be empty.")
            } else {
                val emailPattern = AppUtils.isEmailValid(emailID.text.toString())
                if (!emailPattern) {
                    AppUtils.showMessagePopUp(context, "Enter a Valid Email.")
                } else {
                    if (password.text.toString().equals("")) {
                        AppUtils.showMessagePopUp(context, "Field cannot be empty.")
                    } else {
                        Log.e("email",emailID.text.toString())
                        if (emailID.text.toString().equals("marshal@mobatia.com")){
                            val intent = Intent(context, FireMarshallHomeActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                            finish()
                        }else{

                            if (AppUtils.isInternetAvailable(context)) {
                                // login API\

                                callLoginApi(emailID.text.toString(),password.text.toString())


                            } else {
                                AppUtils.showNetworkErrorPopUp(
                                    context
                                )
                            }
                        }


                    }
                }
            }
        }
        createAccount.setOnClickListener {
//            Toast.makeText(context,"Pressed", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, CreateAccountActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        }
        showHide.setOnClickListener {
            if (passwordShowHide) {
                passwordShowHide = false
                password.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                passwordShowHide = true
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

        }
    }

    private fun callLoginApi(email: String, password: String) {

        var signInResponse: LoginResponseModel
        val call: Call<LoginResponseModel> = ApiClient.getClient.login(
            email,
            password
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                progressBarDialog!!.hide()
                Log.e("status", response.toString())
                if (response.body() == null) {
                    AppUtils.showMessagePopUp(context, "Invalid Credentials")
                } else {
                    if (!response.body()!!.equals("")) {
                        signInResponse = response.body()!!

                        if (signInResponse.status == 200) {
                            if (signInResponse.message.equals("success", ignoreCase = true)) {
//                                AppUtils.showMessagePopUp(context, getString(R.string.text_login_success))
                                staffID = signInResponse.data.staff_id.toString()
                                PreferenceManager.setAccessToken(context, signInResponse.data.token)
                                PreferenceManager.setStaffID(context, staffID)
                                PreferenceManager.setIsFireMarshall(context,true)
                                PreferenceManager.setStaffName(context,signInResponse.data.name)
                                isMarshal = signInResponse.data.is_martial
                                if (isMarshal == "1"){
                                    PreferenceManager.setIsFireMarshall(context,true)
                                }else{
                                    PreferenceManager.setIsFireMarshall(context,false)
                                }
                                showLoginSuccessPopUp(context,getString(R.string.text_login_success))
                                // staff name ??
                                // staff name to pref ??
//                                PreferenceManager.setStaffName(context, staffName)

                            }
                        } else if (signInResponse.status == 422) {
                            AppUtils.showMessagePopUp(
                                context,
                                getString(R.string.text_invalid_cred)
                            )
                        }else if (signInResponse.status == 417) {
                            Log.e("here","else if 417")
                            AppUtils.showMessagePopUp(
                                context,
                                getString(R.string.text_field_missing)
                            )
                        } else {
                            Log.e("here","else")
                            AppUtils.showMessagePopUp(context, getString(R.string.text_unknown_error))
                        }
                    }
                }

            }
            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                progressBarDialog!!.hide()
                AppUtils.showMessagePopUp(context, "Invalid Credentials")
            }


        })
    }

    private fun showLoginSuccessPopUp(context: Context, message: String) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_alert_ok)
            val text = dialog.findViewById<View>(R.id.textDialog) as TextView
            val button = dialog.findViewById<View>(R.id.okButton) as Button
            button.setOnClickListener {
//                if (PreferenceManager.getIsFireMarshall(context)) {
                val intent = Intent(context, SessionSelectActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
//                } else {
//                    val intent = Intent(context, SessionSelectActivity::class.java)
//                    startActivity(intent)
//                    overridePendingTransition(0, 0)
//                    finish()
//                }

            }
            text.text = message
            dialog.show()

    }

    override fun onBackPressed() {
        val intent = Intent(context, WelcomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0,0)
        finish()
    }
}