package com.dngwjy.formapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dngwjy.formapp.R
import com.dngwjy.formapp.data.local.SharedPref
import com.dngwjy.formapp.ui.student.StudentDashboardActivity
import com.dngwjy.formapp.ui.teacher.TeacherDashboardActivity

class SplashScreenActivity : AppCompatActivity() {
    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        handler = Handler()
        doSplash()
    }

    private fun doSplash() {
        handler?.postDelayed({
            when (SharedPref(this).userRole) {
                "teacher" -> {
                    startActivity(Intent(this, TeacherDashboardActivity::class.java))
                }
                "student" -> {
                    startActivity(Intent(this, StudentDashboardActivity::class.java))
                }
                else -> {
                    startActivity(Intent(this, TeacherDashboardActivity::class.java))
                }
            }
            finish()
        }, 1500)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler = null
    }
}