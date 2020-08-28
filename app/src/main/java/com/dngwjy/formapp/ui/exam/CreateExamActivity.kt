package com.dngwjy.formapp.ui.exam

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dngwjy.formapp.R
import com.dngwjy.formapp.data.QuizModel
import com.dngwjy.formapp.ui.exam.create.CreateQuizFragment
import com.dngwjy.formapp.ui.exam.result.ResultQuizFragment
import com.dngwjy.formapp.ui.exam.review.ReviewExamFragment
import com.dngwjy.formapp.ui.exam.share.ShareExamFragment
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_exam.*

class CreateExamActivity : AppCompatActivity(), CreateQuizFragment.OnChangedFragmentListener,
    CreateExamView {

    companion object {
        var examTitle = ""
        var keterangan = ""
        var kategori = ""
        val questionList = mutableListOf<QuizModel>()
        var accessType = ""
        var fragmentPosition = 0
        var startDate = ""
        var endDate = ""
    }

    private val presenter = CreateExamPresenter(FirebaseFirestore.getInstance(), this)

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

        tv_exam_name.text = examTitle
        vp_main.apply {
            adapter = FragmentAdapter(this@CreateExamActivity.supportFragmentManager)
        }
        tl_main.setupWithViewPager(vp_main)
        iv_back.setOnClickListener { onBackPressed() }
        iv_next.setOnClickListener {
            if (fragmentPosition == 2) {
                uploadExamData()
            }
        }

    }

    class FragmentAdapter(fm:FragmentManager):FragmentPagerAdapter(fm){
        private val pages=listOf(
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
           is ReviewExamFragment->{
               logE(frag.toString())
           }
           else->{
               logE(frag.toString())
           }
       }
    }

    override fun onBackPressed() {
        val builder=AlertDialog.Builder(this)
        builder.apply {
            title="Peringatan!"
            setMessage("Anda yakin ingin keluar? semua perubahan anda akan terhapus")
            setPositiveButton("Ya"){dialog, which ->
                dialog.dismiss()
                questionList.clear()
                CreateQuizFragment.questionCount = 0
                super.onBackPressed()
                finish()
            }
            setNegativeButton("tidak") { dialog, which ->
                dialog.dismiss()
            }
        }.create().show()
    }

    fun uploadExamData() {
        presenter.uploadExam(
            examTitle,
            kategori,
            keterangan,
            "",
            accessType,
            "",
            startDate,
            endDate
        )
    }

    fun uploadExamQuestions(examId: String) {

    }

    override fun isLoading(state: Boolean) {

    }

    override fun isError(msg: String) {

    }

    override fun showUploadExamResult(data: String) {
        uploadExamQuestions(data)
    }


    override fun showUploadQuestionResult(data: QuizModel?) {

    }
}