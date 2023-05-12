package com.nas.healthandsafety.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.session_select.SessionSelectActivity
import com.nas.healthandsafety.activity.welcome.WelcomeActivity
import com.nas.healthandsafety.constants.PreferenceManager

class SplashActivity : AppCompatActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context = this
//        AppUtils.getAccessTokenAPICall(context)
        Handler().postDelayed({
            if (PreferenceManager.getStaffID(context) == ""){
                val intent: Intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
//                if (PreferenceManager.getIsFireMarshall(context)) {
//                    // Fire marshall activity
//                    val intent: Intent = Intent(this, FireMarshallHomeActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                } else{
                // Setting same home page for Marshall and Staff User
                val intent: Intent = Intent(this, SessionSelectActivity::class.java)
                startActivity(intent)
                finish()
//                }
            }

//            if (PreferenceManager.getStaffID(context).equals("")) {
//                val intent: Intent = Intent(this, WelcomeActivity::class.java)
//                startActivity(intent)
//                finish()
//            } else {
//                val intent: Intent = Intent(this, SessionSelectActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
        },2000)
    }
}