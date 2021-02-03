package com.example.tracmobilityassessment.ui.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import com.example.tracmobilityassessment.R
import kotlinx.android.synthetic.main.activity_splash.*


class SplashScreenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(
                    applicationContext,
                    MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun startAnimation() {
        val animation: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.splash_animation)
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = Animation.INFINITE
        animation.duration = 2000
        iv_splash.startAnimation(animation)
    }
}