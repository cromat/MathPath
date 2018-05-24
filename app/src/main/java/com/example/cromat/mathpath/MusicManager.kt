package com.example.cromat.mathpath

import java.util.HashMap
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.cromat.mathpath.R

/**
 * This class is used to play sounds across activities, like the
 * background music in our program. Can be extended to play several
 * different sounds.
 **/

object MusicManager {
    private val TAG = "MusicManager"
    private const val MUSIC_PREVIOUS = -1
    private const val MUSIC_BACKGROUND = R.raw.bkground
    private const val MUSIC_GAMEPLAY = 1
    private val players = HashMap<Int, MediaPlayer>()
    private var currentMusic = -1
    private var previousMusic = -1


    private fun getMusicVolume(context: Context): Float {
        //could be updated based on user preferences
        return 1.0f
    }

    @JvmOverloads
    fun start(context: Context, music: Int, force: Boolean = false) {

        var music = music

        if (!force && currentMusic > -1 || currentMusic == music) {
            // already playing some music and not forced to change
            // or already playing this particular music
            return
        }
        if (music == MUSIC_PREVIOUS) {
            Log.d(TAG, "Using previous music [" + previousMusic + "]")
            music = previousMusic
        }
        if (currentMusic != -1) {
            previousMusic = currentMusic
            Log.d(TAG, "Previous music was [" + previousMusic + "]")
            // playing some other music, pause it and change
            pause()
        }
        currentMusic = music
        Log.d(TAG, "Current music is now [" + currentMusic + "]")
        var mp = players[music]
        if (mp != null) {
            if (!mp.isPlaying) {
                mp.start()
            }
        } else {
            if (music == MUSIC_BACKGROUND) {
                mp = MediaPlayer.create(context, R.raw.bkground)
            } else if (music == MUSIC_GAMEPLAY) {
                mp = MediaPlayer.create(context, R.raw.bkground)
            } else {
                Log.e(TAG, "unsupported music number - " + music)
                return
            }
            players[music] = mp
            val volume = getMusicVolume(context)
            Log.d(TAG, "Setting music volume to " + volume)
            try {
                mp.isLooping = true
                mp.start()
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
        }
    }

    fun pause() {
        val mps = players.values
        for (p in mps) {
            if (p.isPlaying) {
                p.pause()
            }
        }
        // previousMusic should always be something valid
        if (currentMusic != -1) {
            previousMusic = currentMusic
            Log.d(TAG, "Previous music was [" + previousMusic + "]")
        }
        currentMusic = -1
        Log.d(TAG, "Current music is now [" + currentMusic + "]")
    }

    fun updateVolumeFromPrefs(context: Context) {
        try {
            val volume = getMusicVolume(context)
            Log.d(TAG, "Setting music volume to " + volume)
            val mps = players.values
            for (p in mps) {
                p.setVolume(volume, volume)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
    }

    fun release() {
        Log.d(TAG, "Releasing media players")
        val mps = players.values
        for (mp in mps) {
            try {
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop()
                    }
                    mp.release()
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
        }
        mps.clear()
        if (currentMusic != -1) {
            previousMusic = currentMusic
            Log.d(TAG, "Previous music was [" + previousMusic + "]")
        }
        currentMusic = -1
        Log.d(TAG, "Current music is now [" + currentMusic + "]")
    }
}
