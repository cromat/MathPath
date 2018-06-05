package com.example.cromat.mathpath.view

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.TextView
import com.example.cromat.mathpath.R


class GoldView : TextView {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.GoldView)
        val img = ContextCompat.getDrawable(context, R.drawable.gold)

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val size: Int = (lineHeight * 0.85).toInt()

        val bitmap = (img as BitmapDrawable).bitmap
        val coinImage = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, size, size, true))
        setCompoundDrawablesWithIntrinsicBounds(null, null, coinImage, null)
        compoundDrawablePadding = 10
        setTypeface(ResourcesCompat.getFont(context, R.font.im_wunderland_cro), Typeface.BOLD)
        setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK)
        setTextColor(Color.WHITE)
        gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
        a.recycle()
    }

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        val imgSize: Int = (lineHeight * 0.85).toInt()
        val img = ContextCompat.getDrawable(context, R.drawable.gold)
        val bitmap = (img as BitmapDrawable).bitmap
        val coinImage = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, imgSize,
                imgSize, true))
        setCompoundDrawablesWithIntrinsicBounds(null, null, coinImage, null)
        compoundDrawablePadding = 10
    }

    override fun setTextSize(size: Float) {
        super.setTextSize(size)
        val imgSize: Int = (lineHeight * 0.85).toInt()
        val img = ContextCompat.getDrawable(context, R.drawable.gold)
        val bitmap = (img as BitmapDrawable).bitmap
        val coinImage = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, imgSize,
                imgSize, true))
        setCompoundDrawablesWithIntrinsicBounds(null, null, coinImage, null)
        compoundDrawablePadding = 10
    }

    fun removeImage(){
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        invalidate()
    }
}
