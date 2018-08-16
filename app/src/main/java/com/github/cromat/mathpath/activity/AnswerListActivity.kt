package com.github.cromat.mathpath.activity

import android.os.Bundle
import com.github.cromat.mathpath.R
import com.github.cromat.mathpath.model.AnswerListAdapter
import kotlinx.android.synthetic.main.activity_answers_list.*

class AnswerListActivity : BgMusicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answers_list)

        val listAnswers = intent.getStringArrayListExtra("listAnswers")
        val eqStr = getString(R.string.equation)
        val yourAnsStr = getString(R.string.your_answer)
        val rightAnsStr = getString(R.string.right_ans)
        listAnswers.add(0, "$eqStr;$yourAnsStr;$rightAnsStr")
        val adapter = AnswerListAdapter(applicationContext, R.layout.answer_list_item, listAnswers)
        listViewAnswers.adapter = adapter
    }
}
