package com.example.cromat.mathpath.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.example.cromat.mathpath.DbHelper
import com.example.cromat.mathpath.MusicManager
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.model.PetItem


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Open Db and create tables if not existing
        DbHelper(applicationContext).writableDatabase

        MusicManager.start(applicationContext, R.raw.bkground)

        val petItems: List<PetItem> = DbHelper.getPetItems(applicationContext)
        petItemDrink.petItem = petItems[0]
        petItemFood.petItem = petItems[1]
        petItemBall.petItem = petItems[2]
        petItemShirt.petItem = petItems[3]

        val goldCurrent = DbHelper.getGoldValue(applicationContext).toString()
        goldViewMain.text = goldCurrent

        btnStart.setOnClickListener {
            startActivityForResult(Intent(applicationContext, PopupDifficultyActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        goldViewMain.text = DbHelper.getGoldValue(applicationContext).toString()
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pause()
    }

    override fun onResume() {
        super.onResume()
        MusicManager.start(applicationContext, R.raw.bkground)
    }
}
