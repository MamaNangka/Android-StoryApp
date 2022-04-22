package com.rchdr.myapplication.view

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.rchdr.myapplication.databinding.ActivitySplashScreenBinding
import com.rchdr.myapplication.view.auth.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var SplashBinding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(SplashBinding.root)

        setupView()
        playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            val it = Intent(this, LoginActivity::class.java)
            startActivity(it)
            finish()
        }, 2000)
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(SplashBinding.splashTv, View.TRANSLATION_Y, 1050f, 1000f).apply {
            duration = 2000
        }.start()
    }
}