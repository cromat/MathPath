package com.example.cromat.mathpath

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder


class BackgroundSoundService : Service() {
    private lateinit var player: MediaPlayer

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.bkground)
        player.isLooping = true
        player.setVolume(100f, 100f)

        val mHomeWatcher = HomeWatcher(this)
        mHomeWatcher.setOnHomePressedListener(object : OnHomePressedListener {
            override fun onHomePressed() {
                stopSelf()
            }

            override fun onHomeLongPressed() {
                stopSelf()
            }
        })
        mHomeWatcher.startWatch()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        player.start()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        player.stop()
        player.release()
    }

    override fun onLowMemory() {
    }

}