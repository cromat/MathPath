package com.example.cromat.mathpath.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.*
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.example.cromat.mathpath.R


class GoldView : TextView {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        textSize = 40f
        val a = context.obtainStyledAttributes(attrs, R.styleable.GoldView)
        val img = ContextCompat.getDrawable(context, R.drawable.gold)
        val size: Int = (lineHeight * 0.85).toInt()
        val bitmap = (img as BitmapDrawable).bitmap
        val d = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, size, size, true))
        setCompoundDrawablesWithIntrinsicBounds(null, null, d, null)
//        context.theme.obtainStyledAttributes(intArrayOf(R.attr.fontFamily))
        compoundDrawablePadding = 10
        setTypeface(ResourcesCompat.getFont(context, R.font.im_wunderland_cro), Typeface.BOLD)
        setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK)
        setTextColor(Color.WHITE)
        gravity = Gravity.CENTER_VERTICAL
        a.recycle()
    }
}
