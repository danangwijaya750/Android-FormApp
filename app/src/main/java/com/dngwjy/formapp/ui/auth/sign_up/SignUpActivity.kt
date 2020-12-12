package com.dngwjy.formapp.ui.auth.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dngwjy.formapp.R
import com.dngwjy.formapp.ui.auth.login.LoginActivity
import com.dngwjy.formapp.util.logE
import com.dngwjy.formapp.util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private val fAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var isStudent = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        tv_masuk.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        iv_back.setOnClickListener {
            onBackPressed()
        }
        btn_signup.setOnClickListener {
            doSignUp()
        }
        btn_guru.setOnClickListener {
            isStudent = false
            changeState()
        }
        btn_siswa.setOnClickListener {
            isStudent = true
            changeState()
        }
    }

    private fun changeState() {
        if (isStudent) {
            btn_siswa.background = resources.getDrawable(R.drawable.bg_rounded)
            btn_siswa.setTextColor(resources.getColor(R.color.colorWhite))
            btn_guru.background = resources.getDrawable(R.drawable.bg_rounded_white)
            btn_guru.setTextColor(resources.getColor(R.color.colorPrimary))
        } else {
            btn_guru.background = resources.getDrawable(R.drawable.bg_rounded)
            btn_guru.setTextColor(resources.getColor(R.color.colorWhite))
            btn_siswa.background = resources.getDrawable(R.drawable.bg_rounded_white)
            btn_siswa.setTextColor(resources.getColor(R.color.colorPrimary))
        }
    }

    private fun doSignUp() {
        pg_loading.visibility = View.VISIBLE
        fAuth.createUserWithEmailAndPassword(
            et_email.text.toString().trim(),
            et_password.text.toString().trim()
        )
            .addOnSuccessListener {
                if (it.user != null) {
                    storeUserData()
                }
            }
            .addOnFailureListener {
                toast(it.localizedMessage)
            }
    }

    private fun storeUserData() {
        val data = hashMapOf<String, String>(
            "uid" to fAuth.currentUser!!.uid,
            "name" to et_name.text.toString(),
            "email" to et_email.text.toString(),
            "kelas" to sp_kelase.selectedItem.toString()
        )
        val role =
            when (isStudent) {
                true -> "col_student"
                else -> "col_teacher"
            }
        db.collection(role).add(data)
            .addOnSuccessListener {
                toast("Registrasi Berhasil")
                pg_loading.visibility = View.GONE
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                toast("Terjadi Kesalahan")
                logE(it.localizedMessage)
                pg_loading.visibility=View.GONE
            }
    }

}