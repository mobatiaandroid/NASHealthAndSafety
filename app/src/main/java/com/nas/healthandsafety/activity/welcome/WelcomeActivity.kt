package com.nas.healthandsafety.activity.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.register.CreateAccountActivity
import com.nas.healthandsafety.activity.login.SignInActivity

class WelcomeActivity : AppCompatActivity() {
    lateinit var getStarted: TextView
    lateinit var context: Context
    lateinit var haveAnAccount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        context = this
        getStarted = findViewById(R.id.getStarted)
        haveAnAccount = findViewById(R.id.haveAnAccount)
        var haveAnAccountString="<font color=#000000>Already Have An Account ? </font> <font color=#EA3056>Sign In</font>"
        haveAnAccount.text = Html.fromHtml(haveAnAccountString)
        haveAnAccount.setOnClickListener {
            val intent = Intent(context, SignInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
//            finish()
        }
        getStarted.setOnClickListener {
            val intent = Intent(context, CreateAccountActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
//            finish()
        }
    }
}