package com.example.cromat.mathpath

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_answers_list.*

class AnswerListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answers_list)

        var listAnswers = intent.getStringArrayListExtra("listAnswers")
        listAnswers.add(0, "Equation;Your Answer;Right Answer")
        val adapter = AnswerListAdapter(applicationContext, R.layout.answer_list_item, listAnswers)
        listViewAnswers.adapter = adapter
    }
}

