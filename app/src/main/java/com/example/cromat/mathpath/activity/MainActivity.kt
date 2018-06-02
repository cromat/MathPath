package com.example.cromat.mathpath.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.example.cromat.mathpath.DbHelper
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.activity.adapters.PetItemAdapter
import com.example.cromat.mathpath.model.PetItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pet_container.*
import kotlinx.android.synthetic.main.activity_main.pet_item_list as petItemList
import com.example.cromat.mathpath.BackgroundSoundService


@SuppressLint("SetTextI18n")
class MainActivity : BgMusicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Open Db and create tables if not existing
        DbHelper(applicationContext).writableDatabase

        // Start music
        startService(Intent(this, BackgroundSoundService::class.java))

        val goldCurrent = DbHelper.getGoldValue(applicationContext).toString()
        goldViewMain.text = goldCurrent

        btnStart.setOnClickListener {
            startActivityForResult(Intent(applicationContext, PopupDifficultyActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        goldViewMain.text = DbHelper.getGoldValue(applicationContext).toString()
    }

    override fun onResume() {
        super.onResume()
        val petItems: List<PetItem> = DbHelper.getPetItems(applicationContext)
        val adapter = PetItemAdapter(petItems)
        petItemList.adapter = adapter
        petItemList.layoutManager = LinearLayoutManager(this)
        checkHappiness()
    }

    fun checkHappiness() {
        val curHappiness = DbHelper.getHapiness(this)
        textHappiness.text = getString(R.string.happiness) + curHappiness.toString() + "%"
        progressHappiness.progress = curHappiness
        when (curHappiness) {
            in 66..100 -> progressHappiness.progressDrawable.setColorFilter(
                    ContextCompat.getColor(this, R.color.colorBgGreen),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            in 34..65 -> progressHappiness.progressDrawable.setColorFilter(
                    ContextCompat.getColor(this, R.color.colorBtnYellow),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            in 0..33 -> progressHappiness.progressDrawable.setColorFilter(
                    ContextCompat.getColor(this, R.color.colorBtnRed),
                    android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }
}
