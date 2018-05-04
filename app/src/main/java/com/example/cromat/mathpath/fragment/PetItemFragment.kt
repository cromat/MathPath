package com.example.cromat.mathpath.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.model.PetItem
import kotlinx.android.synthetic.main.fragment_pet_item.*

private const val PET_ITEM = "pet_item"

class PetItemFragment : Fragment(), View.OnClickListener {
    private var petItem: PetItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_item, container, false)
    }

    public fun updatePetItem(petItemNew: PetItem, context: Context){
        petItemPicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.item_drink))
    }



}
