package com.rchdr.myapplication.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.rchdr.myapplication.R

class EditText : AppCompatEditText {

    var check = ""

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (check == "password") {
                    if (s.length < 6) {
                        error = context.getString(R.string.warn_pass)
                    }
                } else if (check == "email") {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        error = context.getString(R.string.warn_email)
                    }
                } else {
                    if (s.isEmpty()) {
                        error = context.getString(R.string.warn_name)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // nothing
            }
        })
    }

}