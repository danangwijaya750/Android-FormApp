package com.dngwjy.formapp.ui.bank_soal.detail_bank_soal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.ExamModel

class DetailBankSoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_detail_bank_soal)
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}