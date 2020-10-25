package com.dngwjy.formapp.ui.answer

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.model.AnswerModel
import com.dngwjy.formapp.ui.domain.StudentAnswerDomain
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.util.stringToDate
import kotlinx.android.synthetic.main.activity_student_answer.*

class StudentAnswerActivity : AppCompatActivity() {

    private val listAnswer= mutableListOf<AnswerModel?>()
    private val rvAdapter=object:RvAdapter<AnswerModel?>(listAnswer,{

    }){
        override fun layoutId(position: Int, obj: AnswerModel?): Int = R.layout.question_student_view

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = AnswerVH(view)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_student_answer)
        supportActionBar?.hide()
        val data = intent.extras!!["data-answer"] as StudentAnswerDomain?
        rv_answer.apply {
            layoutManager=LinearLayoutManager(this@StudentAnswerActivity,LinearLayoutManager.VERTICAL,false)
            adapter=rvAdapter
        }
        showData(data)
    }
    private fun showData(data:StudentAnswerDomain?){
        tv_exam_name.text=data!!.examName
        tv_name.text= data.studentName
        tv_score.text= data.score.toString()
        tv_date.text= data.takenAt
        if(stringToDate(data.takenAt,"dd-MM-yyyy hh:mm:ss").time>stringToDate(CreateExamActivity.endDate,"dd-MM-yyyy hh:mm").time){
            tv_status.visibility=View.VISIBLE
        }
        listAnswer.clear()
        listAnswer.addAll(data.answers)
        rvAdapter.notifyDataSetChanged()
    }


}