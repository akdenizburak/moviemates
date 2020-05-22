package com.burakakdeniz.moviemates.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.burakakdeniz.moviemates.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var delay:Long=3000
    private lateinit var auth:FirebaseAuth

    private var runnable:Runnable= Runnable {
        if (auth.currentUser!=null){
            val intent =Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
        val intent =Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        init()
    }

    private fun init(){
        handler= Handler()
        handler.postDelayed(runnable,delay)

        auth=FirebaseAuth.getInstance()
    }
}
