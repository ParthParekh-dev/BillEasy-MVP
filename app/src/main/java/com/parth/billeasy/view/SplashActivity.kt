package com.parth.billeasy.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.parth.billeasy.R


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        TransitionManager.beginDelayedTransition(findViewById(R.id.constraintView) as ConstraintLayout)

        val tag1 = findViewById(R.id.tag1) as TextView
        val tag2 = findViewById(R.id.tag2) as TextView

        val handler = Handler()
        handler.postDelayed(Runnable {
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
        }, 3000)

        handler.postDelayed({
            tag1.visibility = View.VISIBLE
        },1000)
        handler.postDelayed({
            tag2.visibility = View.VISIBLE
        },2000)
    }
}