package com.example.cromat.mathpath.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.example.cromat.mathpath.R
import kotlinx.android.synthetic.main.notification_template_lines_media.view.*
import kotlinx.android.synthetic.main.sample_keyboard_view.view.*
import org.jetbrains.anko.childrenSequence

open class KeyboardView : FrameLayout, View.OnClickListener {

    var edtViewAnswer: EditText? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inflate(context, R.layout.sample_keyboard_view, this)
        initViews()
    }

    private fun initViews() {
        for (row in keyboard.childrenSequence())
            for(key in row.childrenSequence()) {
                key.setOnClickListener(this)
            }
    }

    fun setEditText(editText: EditText) {
        this.edtViewAnswer = editText
    }

    override fun onClick(v: View) {
        if ((v as TextView).tag != null && "number_button" == v.tag) {
            if (!edtViewAnswer!!.text.isBlank() && edtViewAnswer!!.text[0] == '0')
                edtViewAnswer!!.setText("0")
            else
                edtViewAnswer!!.append(v.text)
            return
        }
        when (v.id) {
            t9_key_backspace.id -> {
                val editable = edtViewAnswer!!.text
                val charCount = editable.length
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount)
                }
            }
        }
    }
}