package com.example.cromat.mathpath.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.cromat.mathpath.DbHelper
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.activity.adapters.PetItemAdapter
import com.example.cromat.mathpath.model.PetItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pet_container.*
import kotlinx.android.synthetic.main.activity_main.pet_item_list as petItemList


class MainActivity : BgMusicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Open Db and create tables if not existing
        DbHelper(applicationContext).writableDatabase

        // Start music
//        if(!player.isPlaying) {
//            player = MediaPlayer.create(this, R.raw.bkground)
//            player.isLooping = true
//            player.setVolume(100f, 100f)
//            player.start()
//        }


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
    }

}
