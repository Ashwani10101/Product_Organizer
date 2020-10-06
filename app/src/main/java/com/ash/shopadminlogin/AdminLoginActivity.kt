package com.ash.shopadminlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
    }

    fun loginBtnCLick(view: View)
    {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}