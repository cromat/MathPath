package com.github.cromat.mathpath.model

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.github.cromat.mathpath.R


class AnswerListAdapter(context: Context, resource: Int, var items: ArrayList<String>)
    : ArrayAdapter<String>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertViewTmp = convertView

        if (convertViewTmp == null) {
            convertViewTmp = LayoutInflater.from(this.context)
                    .inflate(R.layout.answer_list_item, parent, false)
        }

        val parts = this.items[position].split(";")

        val equation = parts[0]
        val userAns = parts[1]
        val rightAns = parts[2]

        val answerListItemNum = convertViewTmp!!.findViewById<TextView>(R.id.answerListItemNum)
        val answerListItemEquation = convertViewTmp.findViewById<TextView>(R.id.answerListItemEquation)
        val answerListItemUserAns = convertViewTmp.findViewById<TextView>(R.id.answerListItemUserAns)
        val answerListItemRightAns = convertViewTmp.findViewById<TextView>(R.id.answerListItemRightAns)
        val linearAnswerListItem = convertViewTmp.findViewById<LinearLayout>(R.id.linearAnswerListItem)

        if (userAns == rightAns)
            linearAnswerListItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenLight))
        else
            linearAnswerListItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRedLight))

        answerListItemNum.text = "#"
        if (position == 0)
            linearAnswerListItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellowLight))
        else
            answerListItemNum.text = position.toString()

        answerListItemEquation.text = equation.replace("*", "\u02E3").replace("/", "\u00F7")
        answerListItemUserAns.text = userAns
        answerListItemRightAns.text = rightAns

        return convertViewTmp
    }
}
