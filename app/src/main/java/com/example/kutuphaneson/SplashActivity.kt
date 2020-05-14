package com.example.kutuphaneson

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getSupportActionBar()?.hide();


        var asagidanGelenButon= AnimationUtils.loadAnimation(this,R.anim.asagidangelenbuton)

        button1.animation=asagidanGelenButon

        var yukaridanGelenBalon= AnimationUtils.loadAnimation(this,R.anim.yukaridangelenbalon)

        imageView2.animation=yukaridanGelenBalon

        var butongeri = AnimationUtils.loadAnimation(this,R.anim.butongeri)
        var balongeri = AnimationUtils.loadAnimation(this,R.anim.balongeri)

        button1.setOnClickListener{


            button1.startAnimation(butongeri)   //butona basildiginde animasyon basladigi icin burada esitleme yapmiyorsun startanimation kullaniyosun
            imageView2.startAnimation(balongeri)

            val handler = Handler()
            handler.postDelayed({
                var intent = Intent(applicationContext,LoginActivity::class.java)
                startActivity(intent)
            }, 2000)                // 2 saniye delay koydum


        }


    }
}
