package com.example.cromat.mathpath.activity

import android.os.Bundle
import com.example.cromat.mathpath.R
import com.example.cromat.mathpath.model.AnswerListAdapter
import kotlinx.android.synthetic.main.activity_answers_list.*

class AnswerListActivity : BgMusicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answers_list)

        val listAnswers = intent.getStringArrayListExtra("listAnswers")
        listAnswers.add(0, "Equation;Your Answer;Right Answer")
        val adapter = AnswerListAdapter(applicationContext, R.layout.answer_list_item, listAnswers)
        listViewAnswers.adapter = adapter
    }
}
