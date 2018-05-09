package com.example.cromat.mathpath.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.model.PetItem
import kotlinx.android.synthetic.main.fragment_pet_item.*

private const val PET_ITEM = "pet_item"

class PetItemFragment : Fragment(), View.OnClickListener {
    private var petItem: PetItem? = null
    private var viewFrag: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_pet_item, container, false)
        this.viewFrag = rootView
        val petItemFragGoldTextView = rootView.findViewById<TextView>(R.id.textGold)
        val petItemFragGoldImageView = rootView.findViewById<ImageView>(R.id.imageGold)
        petItemFragGoldTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
        petItemFragGoldImageView.layoutParams.width = 70
        petItemFragGoldImageView.layoutParams.height = 70
        return rootView
    }


    override fun onClick(p0: View?) {
    }


    fun updatePetItem(petItemNew: PetItem, context: Context){
        this.petItem = petItemNew
        petItemPicture.setImageDrawable(ContextCompat.getDrawable(context, petItemNew.picture))
        viewFrag!!.findViewById<TextView>(R.id.textGold).text = petItemNew.price.toString()
    }

}
