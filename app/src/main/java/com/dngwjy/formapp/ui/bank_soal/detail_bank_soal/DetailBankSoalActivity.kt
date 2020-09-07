package com.dngwjy.formapp.ui.bank_soal.detail_bank_soal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.ExamModel
import com.dngwjy.formapp.data.QuizModel
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.ui.exam.create.CreateQuizFragment
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_bank_soal.*

class DetailBankSoalActivity : AppCompatActivity(), DetailBankSoalView {
    private val questions = mutableListOf<QuizModel?>()
    private val presenter = DetailBankSoalPresenter(FirebaseFirestore.getInstance(), this)
    private val rvAdapter = object : RvAdapter<QuizModel?>(questions, {}) {
        override fun layoutId(position: Int, obj: QuizModel?): Int = R.layout.question_view

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
            QuestionViewVH(view)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_detail_bank_soal)
        supportActionBar?.hide()

        rv_exam_question.apply {
            adapter=rvAdapter
            val layMan=LinearLayoutManager(this@DetailBankSoalActivity)
            layMan.orientation=LinearLayoutManager.VERTICAL
            layoutManager=layMan
        }

        showData()


        iv_back.setOnClickListener {
            onBackPressed()
        }

    }

    private fun showData() {
        val data = intent.extras!!.get("data-exam") as ExamModel
        tv_exam_name.text = data.title
        tv_category.text = data.category
        tv_subtitle.text = data.desc
        questions.clear()
        presenter.getData(data.id)

        ll_edit.setOnClickListener {
            CreateExamActivity.examTitle = data.title
            CreateExamActivity.kategori = data.category
            CreateExamActivity.keterangan = data.desc
            questions.forEach {
                CreateExamActivity.questionList.add(it!!)
                CreateQuizFragment.questionCount++
            }
            CreateQuizFragment.questionCount++
            startActivity(Intent(this, CreateExamActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onLoading(state: Boolean) {

    }

    override fun onError(msg: String) {
        logE(msg)
    }

    override fun showResult(result: List<QuizModel?>) {
        questions.clear()
        questions.addAll(result)
        rvAdapter.notifyDataSetChanged()
    }

}