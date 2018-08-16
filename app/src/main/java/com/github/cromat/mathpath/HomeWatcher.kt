package com.github.cromat.mathpath

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


class HomeWatcher(context:Context) {
    private val mContext:Context = context
    private val mFilter:IntentFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    private var mListener: OnHomePressedListener? = null
    private var mRecevier: InnerRecevier? = null

    fun setOnHomePressedListener(listener:OnHomePressedListener) {
        mListener = listener
        mRecevier = InnerRecevier()
    }
    fun startWatch() {
        if (mRecevier != null)
        {
            mContext.registerReceiver(mRecevier, mFilter)
        }
    }
    fun stopWatch() {
        if (mRecevier != null)
        {
            mContext.unregisterReceiver(mRecevier)
        }
    }
    internal inner class InnerRecevier:BroadcastReceiver() {
        private val SYSTEM_DIALOG_REASON_KEY = "reason"
        private val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
        private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"

        override fun onReceive(context:Context, intent:Intent) {
            val action = intent.action
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            {
                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                if (reason != null)
                {
                    Log.e(TAG, "action:$action,reason:$reason")
                    if (mListener != null)
                    {
                        if (reason == SYSTEM_DIALOG_REASON_HOME_KEY)
                        {
                            mListener!!.onHomePressed()
                        }
                        else if (reason == SYSTEM_DIALOG_REASON_RECENT_APPS)
                        {
                            mListener!!.onHomeLongPressed()
                        }
                    }
                }
            }
        }
    }
    companion object {
        internal const val TAG = "hg"
    }
}

interface OnHomePressedListener {
    fun onHomePressed()
    fun onHomeLongPressed()
}
