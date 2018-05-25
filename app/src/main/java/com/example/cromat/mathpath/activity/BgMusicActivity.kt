package com.example.cromat.mathpath.activity

import android.app.ActivityManager
import android.content.Context
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import com.example.cromat.mathpath.HomeWatcher
import com.example.cromat.mathpath.OnHomePressedListener


open class BgMusicActivity : AppCompatActivity() {

    var player = MediaPlayer()

    override fun onPause() {
        val mHomeWatcher = HomeWatcher(this)
        mHomeWatcher.setOnHomePressedListener(object : OnHomePressedListener {
            override fun onHomePressed() {
                player.stop()
                System.exit(0)
            }

            override fun onHomeLongPressed() {
                player.stop()
                System.exit(0)
            }
        })
        mHomeWatcher.startWatch()
        super.onPause()
        val context = applicationContext
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo = am.getRunningTasks(1)
        if (!taskInfo.isEmpty()) {
            val topActivity = taskInfo[0].topActivity
            if (topActivity.packageName != context.packageName) {
                player.stop()
            }
        }
    }

}
