package com.example.cromat.mathpath.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.preference.PreferenceActivity
import android.support.v4.content.res.ResourcesCompat
import android.widget.TextView
import com.example.cromat.mathpath.DbHelper
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.fragment.PetItemFragment
import com.example.cromat.mathpath.model.Pet


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val pet = Pet()


        (petItemDrink as PetItemFragment).updatePetItem(pet.petItems[0], applicationContext)


        // Open Db and create tables if not existing
        DbHelper(applicationContext)

        btnStart.setOnClickListener {
            startActivity(Intent(applicationContext, PopupDifficultyActivity::class.java))
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment::class.java!!.getName())
                intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true)
                startActivity(intent)
            }
            R.id.action_stats -> {
                startActivity(Intent(applicationContext, GraphActivity::class.java))
            }
        }
        return true
    }

}
