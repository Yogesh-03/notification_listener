package com.example.flipretail

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flipretail.adapters.AppListAdapter
import com.example.flipretail.databinding.ActivityMainBinding
import com.example.flipretail.notificationlistener.MyNotificationListener


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    private var notificationService: MyNotificationListener? = null
    private var isServiceBound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (isNotificationListenerEnabled()) {
            startNotificationListenerService()
        } else {
            requestNotificationListenerPermission()
        }


    }

    private fun isNotificationListenerEnabled(): Boolean {
        val cn = ComponentName(this, MyNotificationListener::class.java)
        val flat = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        return flat?.contains(cn.flattenToString()) == true
    }

    private fun requestNotificationListenerPermission() {
        // Open settings to enable the notification listener service
        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        Toast.makeText(
            this,
            "Please enable the Notification Listener permission",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun startNotificationListenerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(this, MyNotificationListener::class.java)
            ContextCompat.startForegroundService(this, intent)
        } else {
            // For devices running older Android versions
            startService(Intent(this, MyNotificationListener::class.java))
        }
    }


}