package com.github.cromat.mathpath.view

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.github.cromat.mathpath.DbHelper
import com.github.cromat.mathpath.R
import com.github.cromat.mathpath.activity.MainActivity
import com.github.cromat.mathpath.model.PetItem


class PetItemView : RelativeLayout {
    private var goldView = GoldView(context)
    private val imgView = ImageView(context)

    var petItem: PetItem? = null
        set(petItem) {
            field = petItem
            goldView = this.findViewById(R.id.petItemGold)
            imgView.setImageDrawable(ContextCompat.getDrawable(context, petItem!!.picture))
            if (petItem.price == 9999) {
                goldView.text = context.getString(R.string.coming_soon)
                goldView.removeImage()
            }
            else
                goldView.text = petItem.price.toString()

            if (petItem.bindedElementId > 0) {
                val imgId = petItem.bindedElementId
                val relImg = (context as Activity).findViewById(imgId) as ImageView
                if (petItem.activated && petItem.bought) {
                    alpha = .5f
                    relImg.visibility = View.VISIBLE
                } else {
                    alpha = 1f
                    relImg.visibility = View.INVISIBLE
                }

                if (petItem.bought) {
                    goldView.visibility = View.INVISIBLE
                }
            }
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
        val paramsImg = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imgView.layoutParams = paramsImg
        imgView.setPadding(10, 10, 10, 10)
        addView(imgView)
        invalidate()
        a.recycle()
    }

    fun onClick() {

        fun addHappiness(){
            val ma = context as MainActivity
            DbHelper.addHappiness(petItem!!.happiness, context)
            ma.textCloudThankYou()
        }

        petItem!!.activated = !petItem!!.activated

        if (petItem!!.permanent) {
            if (!petItem!!.bought)
                if (!DbHelper.addGold(petItem!!.price * -1, context))
                    Toast.makeText(context, context.getString(R.string.no_gold), Toast.LENGTH_LONG).show()
                else {
                    addHappiness()
                    val imgId = petItem!!.bindedElementId
                    val relImg = (context as Activity).findViewById(imgId) as ImageView
                    petItem!!.bought = true
                    goldView.visibility = View.INVISIBLE
                    if (petItem!!.activated) {
                        alpha = .5f
                        relImg.visibility = View.VISIBLE
                    } else {
                        alpha = 1f
                        relImg.visibility = View.INVISIBLE
                    }
                }
            else {
                val imgId = petItem!!.bindedElementId
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
            else {
                addHappiness()
            }

        val goldViewMain = (context as Activity).findViewById(R.id.goldViewMain) as GoldView

        // Refresh gold text view
        goldViewMain.text = DbHelper.getGoldValue(context).toString()

        // Update pet item in db
        DbHelper.updatePetItem(petItem!!, context)

        // Refresh this view
        invalidate()
    }
}
