package com.github.cromat.mathpath.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.cromat.mathpath.BackgroundSoundService


open class BgMusicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        onExit()
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        onExit()
        super.onPause()
    }

    override fun onStop() {
        onExit()
        super.onStop()
    }

    override fun onResume() {
        if (!isMyServiceRunning(BackgroundSoundService::class.java)) {
            try {
                startService(Intent(applicationContext, BackgroundSoundService::class.java))
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onResume()
    }

    private fun onExit() {
        if (isApplicationSentToBackground(applicationContext))
            stopService(Intent(applicationContext, BackgroundSoundService::class.java))
    }

    private fun isApplicationSentToBackground(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = tasks[0].topActivity
            if (topActivity.packageName != context.packageName) {
                return true
            }
        }
        return false
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}
