package com.nas.healthandsafety.constants


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nas.healthandsafety.BuildConfig
import com.nas.healthandsafety.R
import com.nas.healthandsafety.activity.home.HomeActivity
import org.json.JSONException
import org.json.JSONObject


class FirebaseMessaging : FirebaseMessagingService() {

    var intent: Intent? = null
    var badgecount: Int = 0

    override fun onNewToken(token: String) {
        PreferenceManager.setFCMID(this, token)
        super.onNewToken(token)
    }

    fun getToken(context: Context): String {
        return PreferenceManager.getFCMID(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        if (remoteMessage.data.isNotEmpty()) {

            try {
                val json = JSONObject(remoteMessage.data.toString())
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e("FIREBASEEXCEPTION:", e.message.toString())

            }
        }



        if (remoteMessage.notification != null) {
            sendNotification(remoteMessage.notification!!.body)
            println("Message Notification Body:" + remoteMessage.notification!!.body)

            //Log.e("FIREBASERECEIVED:", remoteMessage.notification.body)

        }

        super.onMessageReceived(remoteMessage)
    }

    private fun handleDataMessage(json: JSONObject) {

        Log.e("push json:", json.toString())

        try {
            val data = json.getJSONObject("body")
            val badge = data.getString("badge")
            val message = data.getString("message")
            Log.e("FIREBASE_MSG:", message)
            badgecount = badge.toInt()
            sendNotification(message)
        } catch (e: JSONException) {
            Log.e("JSONEXCEPTION:", e.message.toString())

        } catch (e: java.lang.Exception) {

        }

    }

    private fun sendNotification(messageBody: String?) {
        intent = Intent(this, HomeActivity::class.java)
        intent!!.action = System.currentTimeMillis().toString()
        intent!!.putExtra("Notification_Recieved", 1)
        val notId = NotificationID.iD
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent!!)
        /* val pendingIntent = stackBuilder.getPendingIntent(notId,
             PendingIntent.FLAG_IMMUTABLE || PendingIntent.FLAG_UPDATE_CURRENT)*/
        val pendingIntent = stackBuilder.getPendingIntent(
            notId,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val c_sound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.alertsound)

//        val notificationSoundUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.alertsound)
//        val soundNotify = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.alertsound);
//        val resourceId = R.raw.alertsound
//        val uri = Uri.parse("android.resource://" + packageName + "/" + resourceId)
//        Log.e("notificati sounf", defaultSoundUri.toString())
//        Log.e("notificationSoundUri sounf", notificationSoundUri.toString())
//        Log.e("notificati", soundNotify.toString())
//        Log.e("notificati", uri.toString())

        val notificationBuilder =
            NotificationCompat.Builder(this)
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(c_sound)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel_id = getString(R.string.app_name) + "_01"
            val name: CharSequence = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channel_id, name, importance)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            notificationBuilder.setChannelId(mChannel.id)
            mChannel.setShowBadge(true)
            mChannel.canShowBadge()
            mChannel.enableLights(true)
            mChannel.lightColor = resources.getColor(R.color.pink)
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            mChannel.setSound(c_sound, audioAttributes)
            notificationManager.createNotificationChannel(mChannel)
        }

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setSmallIcon(R.drawable.notify_small);
//            notificationBuilder.color = getResources().getColor(R.color.split_bg);
//        } else {
//            notificationBuilder.setSmallIcon(R.drawable.notify_small);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.evacuation_icon)
            notificationBuilder.color = resources.getColor(R.color.pink)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.evacuation_icon)
            notificationBuilder.color = resources.getColor(R.color.pink)
        }

        notificationManager.notify(notId, notificationBuilder.build())

    }
}