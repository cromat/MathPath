package com.github.cromat.mathpath.activity

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager
import com.github.cromat.mathpath.R


class PopupInfoActivity : BgMusicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_info)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width: Int = dm.widthPixels
        val height: Int = dm.heightPixels
        val intent = Intent()
        setResult(1, intent)
        window.setLayout((width * .8).toInt(), (height * .7).toInt())
        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        window.attributes = params
    }
}
