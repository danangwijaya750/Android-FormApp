package com.dngwjy.formapp.ui.auth.login

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dngwjy.formapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

    }
}