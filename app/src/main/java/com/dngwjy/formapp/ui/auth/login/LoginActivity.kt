package com.dngwjy.formapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dngwjy.formapp.R
import com.dngwjy.formapp.ui.auth.sign_up.SignUpActivity
import com.dngwjy.formapp.util.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val fAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        btn_login.setOnClickListener {
            if (et_email.text.toString().isBlank() && et_password.text.toString().isBlank()) {
                fAuth.signInWithEmailAndPassword(
                    et_email.text.toString(),
                    et_password.text.toString()
                )
                    .addOnSuccessListener {
                        onBackPressed()
                    }
                    .addOnFailureListener {
                        toast("Error ${it.localizedMessage}")
                    }
            }
        }
        tv_daftar.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}