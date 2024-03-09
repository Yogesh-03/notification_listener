package com.example.flipretail.notificationlistener

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import androidx.core.app.ServiceCompat.stopForeground
import com.example.flipretail.R
import java.util.Locale


class MyNotificationListener : NotificationListenerService(), OnInitListener {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var textToSpeech: TextToSpeech

    companion object {
        private const val NOTIFICATION_ID = 1
        var text: String? = "Hello"
    }

    override fun onListenerConnected() {
        // Request notification listener hints when the service is connected
        requestListenerHints(NotificationListenerService.HINT_HOST_DISABLE_EFFECTS)

        // Start the service in the foreground with a dummy notification
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        Log.d("NotificationListener", " Connected")

        mediaPlayer = MediaPlayer.create(this, R.raw.notification_sound)
        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener() {

        })

    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Called when a new notification is posted
        Log.d("NotificationListener", "onNotificationPosted: ${sbn.packageName}")
        if (sbn.packageName == "com.whatsapp"){
            text = "Message received on Whatsapp " + sbn.notification.extras.getString("android.title")
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        mediaPlayer.start()
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Called when a notification is removed
        Log.d("NotificationListener", "onNotificationRemoved: ${sbn.packageName}")
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MyNotificationListener::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channelId",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        return Notification.Builder(this, "channelId")
            .setContentTitle("Notification Listener Service")
            .setContentText("Running in the background")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onInit(p0: Int) {
        if (p0!=TextToSpeech.ERROR){
            textToSpeech.setLanguage(Locale.ENGLISH)
        }
    }


}





