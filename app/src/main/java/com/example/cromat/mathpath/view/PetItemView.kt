package com.example.cromat.mathpath.view

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.model.PetItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.cromat.mathpath.DbHelper


class PetItemView : RelativeLayout {
    val goldView = GoldView(context)
    private val imgView = ImageView(context)
    var petItem: PetItem? = null
        set(petItem) {
            field = petItem
            imgView.setImageDrawable(ContextCompat.getDrawable(context, petItem!!.picture))
            goldView.text = petItem.price.toString()

            if (petItem.bindedElementId > 0) {
                val imgId = petItem.bindedElementId as Int
                val relImg = (context as Activity).findViewById(imgId) as ImageView
                if (petItem.activated) {
                    alpha = .5f
                    relImg.visibility = View.VISIBLE
                } else {
                    alpha = 1f
                    relImg.visibility = View.INVISIBLE
                }
            }
            invalidate()

            invalidate()
        }



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
        val a = context.obtainStyledAttributes(attrs, R.styleable.PetItemView)
        background = ContextCompat.getDrawable(context, R.drawable.rounded_shape)
        gravity = Gravity.CENTER
        goldView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
        imgView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ball))

        val paramsImg = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 200)
        imgView.layoutParams = paramsImg
        imgView.setPadding(10, 10, 10, 10)

        val paramsGold = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 70)
        paramsGold.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        paramsGold.addRule(RelativeLayout.ALIGN_BOTTOM)
        paramsGold.addRule(RelativeLayout.CENTER_HORIZONTAL)
        goldView.layoutParams = paramsGold
        goldView.text = "0"

        addView(imgView)
        addView(goldView)
        invalidate()
        a.recycle()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        OnClick()
        return super.onTouchEvent(event)
    }

    override fun callOnClick(): Boolean {
        OnClick()
        return super.callOnClick()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        OnClick()
        super.setOnClickListener(l)
    }

    fun OnClick() {
        petItem!!.activated = !petItem!!.activated

        if (petItem!!.permanent) {
            if (!petItem!!.bought)
                if (!DbHelper.addGold(petItem!!.price * -1, context))
                    Toast.makeText(context, context.getString(R.string.no_gold), Toast.LENGTH_LONG).show()
                else {
                    val imgId = petItem!!.bindedElementId as Int
                    val relImg = (context as Activity).findViewById(imgId) as ImageView
                    petItem!!.bought = true
                    if (petItem!!.activated) {
                        alpha = .5f
                        relImg.visibility = View.VISIBLE
                    } else {
                        alpha = 1f
                        relImg.visibility = View.INVISIBLE
                    }
                }
            else {
                val imgId = petItem!!.bindedElementId as Int
                val relImg = (context as Activity).findViewById(imgId) as ImageView
                if (petItem!!.activated) {
                    alpha = .5f
                    relImg.visibility = View.VISIBLE
                } else {
                    alpha = 1f
                    relImg.visibility = View.INVISIBLE
                }
            }
        } else
            if (!DbHelper.addGold(petItem!!.price * -1, context))
                Toast.makeText(context, context.getString(R.string.no_gold), Toast.LENGTH_LONG).show()

        val goldViewMain = (context as Activity).findViewById(R.id.goldViewMain) as GoldView

        // Refresh gold text view
        goldViewMain.text = DbHelper.getGoldValue(context).toString()

        // Update pet item in db
        DbHelper.updatePetItem(petItem!!, context)

        // Refresh this view
        invalidate()
    }


}
