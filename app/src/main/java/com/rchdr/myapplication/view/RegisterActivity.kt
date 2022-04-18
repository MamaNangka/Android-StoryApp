package com.rchdr.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rchdr.myapplication.R
import com.rchdr.myapplication.databinding.ActivityLoginBinding
import com.rchdr.myapplication.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var RegisterBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(RegisterBinding.root)

        RegisterBinding.registerTv2.setOnClickListener {
            Intent(this@RegisterActivity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}