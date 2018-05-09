package com.example.cromat.mathpath.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cromat.mathpath.DbHelper
import com.example.cromat.mathpath.R


class GoldFragment : Fragment() {
    var textGold: TextView? = null
    var viewFrag: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_gold, container, false)
        this.viewFrag = rootView
        return rootView
    }

    fun setText(value: String){
        this.viewFrag!!.findViewById<TextView>(R.id.textGold).text = value
    }
}
