package com.example.cromat.mathpath

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.ArrayAdapter
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout


class AnswerListAdapter: ArrayAdapter<String> {

    var items: ArrayList<String>

    constructor(context: Context, resource: Int, items: ArrayList<String>) : super(context, resource, items){
        this.items = items
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(this.context)
                    .inflate(R.layout.answer_list_item, parent, false)
        }

        val parts = this.items[position].split(";")

        val equation = parts[0]
        val userAns = parts[1]
        val rightAns = parts[2]

        val answerListItemNum = convertView!!.findViewById<TextView>(R.id.answerListItemNum)
        val answerListItemEquation = convertView.findViewById<TextView>(R.id.answerListItemEquation)
        val answerListItemUserAns = convertView.findViewById<TextView>(R.id.answerListItemUserAns)
        val answerListItemRightAns = convertView.findViewById<TextView>(R.id.answerListItemRightAns)
        val linearAnswerListItem = convertView.findViewById<LinearLayout>(R.id.linearAnswerListItem)

        if (userAns == rightAns)
            linearAnswerListItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenLight))
        else
            linearAnswerListItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRedLight))

        answerListItemNum.text = "#"
        if (position == 0)
            linearAnswerListItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrayLight))
        else
            answerListItemNum.text = (position + 1).toString()

        answerListItemEquation.text = equation
        answerListItemUserAns.text = userAns
        answerListItemRightAns.text = rightAns

        return convertView!!
    }
}
