package com.dngwjy.formapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dngwjy.formapp.R
import com.dngwjy.formapp.data.local.SharedPref
import com.dngwjy.formapp.ui.auth.sign_up.SignUpActivity
import com.dngwjy.formapp.ui.student.StudentDashboardActivity
import com.dngwjy.formapp.ui.teacher.TeacherDashboardActivity
import com.dngwjy.formapp.util.logE
import com.dngwjy.formapp.util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val fAuth = FirebaseAuth.getInstance()
    private val db=FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        btn_login.setOnClickListener {
            if (!et_email.text.toString().isBlank() && !et_password.text.toString().isBlank()) {
                pg_loading.visibility= View.VISIBLE
                fAuth.signInWithEmailAndPassword(
                    et_email.text.toString(),
                    et_password.text.toString()
                )
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            checkRole()
                        }
                        else{
                            logE(it.exception.toString())
                            toast("Error ${it.exception.toString()}")
                            pg_loading.visibility= View.GONE
                        }
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

    private fun checkRole(){
        db.collection("col_student").whereEqualTo("uid",fAuth.currentUser!!.uid)
            .get().addOnFailureListener {
                logE(it.localizedMessage)
            }
            .addOnSuccessListener {
                var userClass=""
             val callIntent=
                 when(it.isEmpty){
                     true-> {
                         Intent(this, TeacherDashboardActivity::class.java)
                     }
                     else->{
                         userClass=it.documents[0]["kelas"] as String
                         Intent(this,StudentDashboardActivity::class.java)
                     }
                 }
                toast("Login Berhasil!")
                SharedPref(this).userRole=
                    when(it.isEmpty){
                        true->"teacher"
                        else->"student"
                    }
                SharedPref(this).userClass=userClass
                pg_loading.visibility=View.GONE
                startActivity(callIntent)
                finish()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}