package com.example.cromat.mathpath.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.example.cromat.mathpath.DbHelper
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.fragment.GoldFragment
import com.example.cromat.mathpath.fragment.PetItemFragment
import com.example.cromat.mathpath.model.Pet
import kotlinx.android.synthetic.main.fragment_gold.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Open Db and create tables if not existing
        DbHelper(applicationContext).writableDatabase

        val pet = Pet()
        (petItemDrink as PetItemFragment).updatePetItem(pet.petItems[0], applicationContext)
        (petItemFood as PetItemFragment).updatePetItem(pet.petItems[1], applicationContext)
        (petItemBall as PetItemFragment).updatePetItem(pet.petItems[2], applicationContext)
        (petItemShirt as PetItemFragment).updatePetItem(pet.petItems[3], applicationContext)

        val goldCurrent = DbHelper.getGoldValue(applicationContext).toString()
        (goldFragmentMain as GoldFragment).setText(goldCurrent)

        btnStart.setOnClickListener {
            startActivityForResult(Intent(applicationContext, PopupDifficultyActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        textGold.text = DbHelper.getGoldValue(applicationContext).toString()
    }
}
