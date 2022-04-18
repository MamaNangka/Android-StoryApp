package com.rchdr.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rchdr.myapplication.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var LoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(LoginBinding.root)

        LoginBinding.loginTv2.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}