package com.nas.healthandsafety.constants

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.session_select.SessionSelectActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppUtils {
    companion object{
        fun showMessagePopUp(context: Context, message: String) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_alert)
            val text = dialog.findViewById<View>(R.id.textDialog) as TextView
            text.text = message
            dialog.show()
        }


        fun showNetworkErrorPopUp(context: Context){
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_network_error)
            val text = dialog.findViewById<View>(R.id.textDialog) as TextView
            text.text = context.getString(R.string.text_network_error)
        }

        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isInternetAvailable(context: Context): Boolean
        {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }
//        fun getAccessTokenAPICall(context: Context) {
//            val call: Call<ResponseBody> = ApiClient.getClient.accessToken(
//                "password",
//                "testclient",
//                "testpass",
//                "krishnaraj.s@mobatia.com",
//                "admin123"
//            )
//            call.enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    val responseData = response.body()
//                    if (responseData != null) {
//                        val jsonObject = JSONObject(responseData.string())
//                        Log.e("Response",response.body().toString())
//                        val accessToken: String = jsonObject.optString("access_token")
//                        PreferenceManager.setAccessToken(context, accessToken)
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    showMessageOKPopUp(context, "Invalid Grant")
//                }
//
//            })
//        }

        fun markAttendanceFound(id: String) {
//            val databaseReference = FirebaseDatabase.getInstance().reference.child("evacuations")
//            databaseReference.addValueEventListener(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    databaseReference.child("-MifUGjqDwIm397no97D").addValueEventListener(object:
//                        ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (snapshot in snapshot.children){
//                                if (snapshot.child("class_id").value  != null) {
//                                    if ((snapshot.child("id").value)!!.equals(id)){
//                                        val studentItem = Post(
//                                            "1",
//                                            snapshot.child("found").value.toString(),
//                                            snapshot.child("id").value.toString(),
//                                            snapshot.child("photo").value.toString(),
//                                            snapshot.child("present").value.toString(),
//                                            snapshot.child("registration_id").value.toString(),
//                                            snapshot.child("assembly_point").value.toString(),
//                                            snapshot.child("assembly_point_id").value.toString(),
//                                            snapshot.child("class_id").value.toString(),
//                                            snapshot.child("class_name").value.toString(),
//                                            snapshot.child("created_at").value.toString(),
//                                            snapshot.child("section").value.toString(),
//                                            snapshot.child("staff_id").value.toString(),
//                                            snapshot.child("student_name").value.toString(),
//                                            snapshot.child("subject").value.toString(),
//                                            snapshot.child("updated_at").value.toString())
//                                        val postValues: Map<String, Any> = studentItem.toMap() as Map<String, Any>
//                                        databaseReference.child("-MifUGjqDwIm397no97D").child(snapshot.child("id").value.toString()).updateChildren(postValues)
//                                            .addOnSuccessListener { Log.e("Success","Success") }
//
//                                    }
//                                } else {
//                                    break
//                                }
//                            }
//
//                        }
//                        override fun onCancelled(error: DatabaseError) {}
//                    })
//                }
//                override fun onCancelled(error: DatabaseError) {}
//            })
        }

        fun markAttendanceNotFound(id: String) {
//            val databaseReference = FirebaseDatabase.getInstance().reference.child("evacuations")
//            databaseReference.addValueEventListener(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    databaseReference.child("-MifUGjqDwIm397no97D").addValueEventListener(object:
//                        ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (snapshot in snapshot.children){
//                                if (snapshot.child("class_id").value  != null) {
//                                    Log.e("Not Found", databaseReference.child("-MifUGjqDwIm397no97D")
//                                        .child(snapshot.child("id").toString())
//                                        .child("found").toString()
//                                    )
//                                    if ((snapshot.child("id").value)!!.equals(id)){
//                                        val studentItem = Post(
//                                            "0",
//                                            snapshot.child("found").value.toString(),
//                                            snapshot.child("id").value.toString(),
//                                            snapshot.child("photo").value.toString(),
//                                            snapshot.child("present").value.toString(),
//                                            snapshot.child("registration_id").value.toString(),
//                                            snapshot.child("assembly_point").value.toString(),
//                                            snapshot.child("assembly_point_id").value.toString(),
//                                            snapshot.child("class_id").value.toString(),
//                                            snapshot.child("class_name").value.toString(),
//                                            snapshot.child("created_at").value.toString(),
//                                            snapshot.child("section").value.toString(),
//                                            snapshot.child("staff_id").value.toString(),
//                                            snapshot.child("student_name").value.toString(),
//                                            snapshot.child("subject").value.toString(),
//                                            snapshot.child("updated_at").value.toString())
//                                        val postValues: Map<String, Any> = studentItem.toMap() as Map<String, Any>
//                                        databaseReference.child("-MifUGjqDwIm397no97D").child(snapshot.child("id").value.toString()).updateChildren(postValues)
//                                            .addOnSuccessListener { Log.e("Success","Success") }
//                                    }
//                                } else {
//                                    break
//                                }
//                            }
//
//                        }
//                        override fun onCancelled(error: DatabaseError) {}
//                    })
//                }
//                override fun onCancelled(error: DatabaseError) {}
//            })
        }
        fun setPos(position: Int){
//            pos = position
        }
//        fun getPos(): Int {
//            return pos
//        }

    }

}
