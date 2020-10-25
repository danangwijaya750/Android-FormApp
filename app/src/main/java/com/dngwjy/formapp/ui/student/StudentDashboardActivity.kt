package com.dngwjy.formapp.ui.student

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.local.SharedPref
import com.dngwjy.formapp.data.model.ExamModel
import com.dngwjy.formapp.ui.auth.login.LoginActivity
import com.dngwjy.formapp.ui.bank_soal.detail_bank_soal.DetailBankSoalActivity
import com.dngwjy.formapp.util.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.synthetic.main.activity_student_dashboard.*

class StudentDashboardActivity : AppCompatActivity() {
    private val db=FirebaseFirestore.getInstance()
    private val relatedExam= mutableListOf<ExamModel?>()
    private val populerExam= mutableListOf<ExamModel?>()
    private lateinit var pref:SharedPref

    private val popularAdapter=object : RvAdapter<ExamModel?>(populerExam
        ,{
            handleClick(it)
        }){
        override fun layoutId(position: Int, obj: ExamModel?): Int = R.layout.item_populer_exam

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = PopularExamVH(view)
    }
    private val relatedAdapter=object : RvAdapter<ExamModel?>(relatedExam
        ,{
            handleClick(it)
        }){
        override fun layoutId(position: Int, obj: ExamModel?): Int = R.layout.item_populer_exam

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = PopularExamVH(view)
    }

    private fun handleClick(data:ExamModel?){
        val intent= Intent(this,DetailBankSoalActivity::class.java)
        intent.putExtra("data-exam",data)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_student_dashboard)
        supportActionBar?.hide()
        pref= SharedPref(this)

        rv_ujian_populer.apply {
            adapter=popularAdapter
            val layManager=LinearLayoutManager(this@StudentDashboardActivity)
            layManager.orientation=LinearLayoutManager.VERTICAL
            layoutManager=layManager
        }
        rv_ujian_anda.apply{
            adapter=relatedAdapter
            val layManager=LinearLayoutManager(this@StudentDashboardActivity)
            layManager.orientation=LinearLayoutManager.VERTICAL
            layoutManager=layManager
        }
        btn_profile.text= SharedPref(this).userName[0].toString()
        btn_profile.setOnClickListener {
            val ballon = Balloon.Builder(this)
                .setLayout(R.layout.layout_logout_dialog)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setWidthRatio(0f)
                .setHeight(60)
                .setCornerRadius(4f)
                .setArrowPosition(0.5f)
                .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                .build()
            ballon.showAlignBottom(it)
            ballon.getContentView().findViewById<LinearLayout>(R.id.ll_keluar)
                .setOnClickListener {
                    FirebaseAuth.getInstance().signOut()
                    pref.userRole=""
                    pref.userClass=""
                    pref.userEmail=""
                    pref.userName=""
                    pref.userPass=""
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
        }

        getRelatedExam()
        getPopularExam()
    }
    private fun getRelatedExam(){
        relatedExam.clear()
        db.collection("col_exam")
            .whereEqualTo("kelas",pref.userClass)
            .whereEqualTo("accessType","Publik")
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    it.forEach { snap->
                        relatedExam.add(snap.toObject(ExamModel::class.java))
                    }
                    notifyRelatedExam()
                }
            }
            .addOnFailureListener {
                logE(it.localizedMessage)
            }
    }
    private fun getPopularExam(){
        populerExam.clear()
        db.collection("col_exam")
            .whereEqualTo("accessType","Publik")
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    it.forEach { snap->
                        populerExam.add(snap.toObject(ExamModel::class.java))
                    }
                    notifyPopularExam()
                }
            }
            .addOnFailureListener {
                logE(it.localizedMessage)
            }
    }
    private fun notifyRelatedExam(){
        relatedAdapter.notifyDataSetChanged()
    }
    private fun notifyPopularExam(){
        popularAdapter.notifyDataSetChanged()
    }

}