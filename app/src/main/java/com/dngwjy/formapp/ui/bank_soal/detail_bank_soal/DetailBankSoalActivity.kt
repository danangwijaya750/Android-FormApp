package com.dngwjy.formapp.ui.bank_soal.detail_bank_soal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.ExamModel
import com.dngwjy.formapp.data.QuizModel
import kotlinx.android.synthetic.main.activity_detail_bank_soal.*

class DetailBankSoalActivity : AppCompatActivity() {
    private val questions= mutableListOf<QuizModel>()
    private val rvAdapter=object:RvAdapter<QuizModel>(questions,{})
    {
        override fun layoutId(position: Int, obj: QuizModel): Int =R.layout.question_view_layout

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = QuestionViewVH(view)

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

    private fun showData(){
        val data=intent.extras!!.get("data-exam") as ExamModel
        tv_exam_name.text=data.title
        tv_category.text=data.category
        tv_subtitle.text=data.desc
        questions.clear()
        data.quizes.forEach {
            questions.add(it!!)
        }
        rvAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}