package com.dngwjy.formapp.ui.exam

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dngwjy.formapp.R
import com.dngwjy.formapp.data.local.SharedPref
import com.dngwjy.formapp.data.model.ExamModel
import com.dngwjy.formapp.data.model.QuizModel
import com.dngwjy.formapp.ui.bank_soal.detail_bank_soal.DetailBankSoalActivity
import com.dngwjy.formapp.ui.exam.create.CreateQuizFragment
import com.dngwjy.formapp.ui.exam.result.ResultQuizFragment
import com.dngwjy.formapp.ui.exam.review.ReviewExamFragment
import com.dngwjy.formapp.ui.exam.share.ShareExamFragment
import com.dngwjy.formapp.util.logD
import com.dngwjy.formapp.util.logE
import com.dngwjy.formapp.util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_exam.*
import java.io.ByteArrayOutputStream


class CreateExamActivity : AppCompatActivity(), CreateQuizFragment.OnChangedFragmentListener,
    CreateExamView {

    companion object {
        var examTitle = ""
        var keterangan = ""
        var kategori = ""
        val questionList = mutableListOf<QuizModel?>()
        var accessType = ""
        var fragmentPosition = 0
        var startDate = ""
        var endDate = ""
        var totalScore = 0
        var bitmap: Bitmap? = null
        var questionPositions = 0
        var tags = mutableListOf<String>()
        var uploaded = false
        var examId = ""

    }

    private val presenter = CreateExamPresenter(FirebaseFirestore.getInstance(), this)
    private val fAuth= FirebaseAuth.getInstance()

    override fun onAttachFragment(fragment: Fragment) {
        if(fragment is CreateQuizFragment){
            fragment.setOnHeadlineSelectedListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_create_exam)
        supportActionBar?.hide()
        vp_main.apply {
            adapter = FragmentAdapter(this@CreateExamActivity.supportFragmentManager)
        }
        tl_main.setupWithViewPager(vp_main)
        if (intent.hasExtra("data-exam")) {
            logE("dataexam")
            val dataExam = intent.extras!!["data-exam"] as ExamModel
            uploaded = true
            examId = dataExam.id
            examTitle = dataExam.title
            presenter.getQuizData(examId)
        }

        tv_exam_name.text = examTitle
        iv_back.setOnClickListener { onBackPressed() }
        iv_next.setOnClickListener {
            if (fragmentPosition == 2 && !uploaded) {
                uploadExamData()
            }
        }

    }


    class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val pages = listOf(
            CreateQuizFragment.getInstance(),
            ReviewExamFragment.getInstance(),
            ShareExamFragment.getInstance(),
            ResultQuizFragment.getInstance()
        )

        private val pagesTitle = listOf("BUAT", "REVIEW", "SHARE", "HASIL")

        override fun getItem(position: Int): Fragment = pages[position]

        override fun getCount(): Int =pages.size

        override fun getPageTitle(position: Int): CharSequence? = pagesTitle[position]
    }

    override fun onChanged() {
       when(val frag = supportFragmentManager.findFragmentById(R.id.vp_main)){
           is ReviewExamFragment -> {
               logE(frag.toString())
           }
           else -> {
               logE(frag.toString())
           }
       }
    }

    override fun onBackPressed() {
        if (!uploaded) {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                title = "Peringatan!"
                setMessage("Anda yakin ingin keluar? semua perubahan anda akan terhapus")
                setPositiveButton("Ya") { dialog, which ->
                    dialog.dismiss()
                    questionList.clear()
                    bitmap = null
                    CreateQuizFragment.questionCount = 0
                    super.onBackPressed()
                    finish()
                }
                setNegativeButton("tidak") { dialog, which ->
                    dialog.dismiss()
                }
            }.create().show()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    fun uploadExamData() {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray? = baos.toByteArray()
        presenter.uploadExam(
            examTitle,
            kategori,
            keterangan,
            "",
            accessType,
            "",
            startDate,
            endDate,
            data,
            fAuth.currentUser!!.uid,
            SharedPref(this).userClass,
            tags
        )
    }

    private fun uploadQuestions(examId: String) {
        if (questionPositions < questionList.size) {
            presenter.uploadQuestion(examId, questionList[questionPositions]!!)
            questionPositions++
        } else {
            logD("Questions uploaded")
            isLoading(false)
            showMessage(examId)
        }
    }
    private fun showMessage(examId:String){
        val alertDialogBuilder = AlertDialog.Builder(this)
        val alertLayout = layoutInflater.inflate(R.layout.layout_succes_upload, null)
        alertDialogBuilder.setView(alertLayout)
        alertDialogBuilder.setCancelable(false)
        val btnCancel = alertLayout.findViewById<ImageView>(R.id.btn_close)
        val btnSubmit = alertLayout.findViewById<Button>(R.id.btn_success_dialog)
        val dialog = alertDialogBuilder.create()
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnSubmit.setOnClickListener {
            dialog.dismiss()
            val callIntent=Intent(this, DetailBankSoalActivity::class.java)
            callIntent.putExtra("exam-id",examId)
            startActivity(callIntent)
        }
        dialog.show()
    }


    override fun isLoading(state: Boolean) {
        when (state) {
            true -> pg_loading.visibility = View.VISIBLE
            else -> pg_loading.visibility = View.GONE
        }
    }

    override fun isError(msg: String) {
        toast("Terjadi Error : $msg")
    }

    override fun showUploadExamResult(data: String) {
        uploadQuestions(data)
    }


    override fun showUploadQuestionResult(data: QuizModel?) {

    }

    override fun showGetQuizResult(data: List<QuizModel?>) {
        questionList.clear()
        questionList.addAll(data)
        CreateQuizFragment.rvAdapter.notifyDataSetChanged()
        ReviewExamFragment.rvAdapter.notifyDataSetChanged()
    }
}