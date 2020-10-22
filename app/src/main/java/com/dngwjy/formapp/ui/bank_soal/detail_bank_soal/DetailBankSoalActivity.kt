package com.dngwjy.formapp.ui.bank_soal.detail_bank_soal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.local.SharedPref
import com.dngwjy.formapp.data.model.ExamModel
import com.dngwjy.formapp.data.model.QuizModel
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.ui.exam.create.CreateQuizFragment
import com.dngwjy.formapp.ui.quiz.QuizActivity
import com.dngwjy.formapp.util.logE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_bank_soal.*

class DetailBankSoalActivity : AppCompatActivity(), DetailBankSoalView {
    private val questions = mutableListOf<QuizModel?>()
    private val presenter = DetailBankSoalPresenter(FirebaseFirestore.getInstance(), this)
    private val fAuth = FirebaseAuth.getInstance()
    private lateinit var dataExam: ExamModel
    private val rvAdapter = object : RvAdapter<QuizModel?>(questions, {
        handleClick(it!!)
    }) {
        override fun layoutId(position: Int, obj: QuizModel?): Int = R.layout.question_view

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
            QuestionViewVH(view)

    }


    private fun handleClick(data:QuizModel){
        if(fAuth.currentUser!=null && SharedPref(this).userRole=="teacher"){
            CreateExamActivity.questionList
                .add(QuizModel(CreateQuizFragment.questionCount,
                    data.question,data.choice,data.questionType,data.answer,data.score))
            CreateQuizFragment.questionCount++
            finish()
        }
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

        if(intent.hasExtra("exam-id")){
            presenter.getExamData(intent.extras!!.get("exam-id") as String)
        }else{
            showData(intent.extras!!.get("data-exam") as ExamModel)
        }


        iv_back.setOnClickListener {
            onBackPressed()
        }
        changeView()

    }
    private fun changeView(){
        when(SharedPref(this).userRole){
            "teacher"->{
                ll_edit.visibility=View.VISIBLE
                ll_simpan.visibility=View.VISIBLE
                ll_mulai.visibility=View.GONE
            }
            "student" -> {
                ll_mulai.visibility = View.VISIBLE
                ll_edit.visibility = View.GONE
                ll_simpan.visibility = View.GONE
                ll_mulai.setOnClickListener {
                    if (this::dataExam.isInitialized) {
                        val intent = Intent(this, QuizActivity::class.java)
                        intent.putExtra("data-quiz", dataExam)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun showData(data :ExamModel?) {

        tv_exam_name.text = data!!.title
        tv_category.text = data.category
        tv_subtitle.text = data.desc
        logE(data.image)
        Glide.with(this)
            .load(data.image)
            .centerCrop()
            .into(iv_exam_image)
        questions.clear()
        presenter.getData(data.id)

//        ll_edit.setOnClickListener {
//            CreateExamActivity.examTitle = data.title
//            CreateExamActivity.kategori = data.category
//            CreateExamActivity.keterangan = data.desc
//            questions.forEach {
//                CreateExamActivity.questionList.add(it!!)
//                CreateQuizFragment.questionCount++
//            }
//            CreateQuizFragment.questionCount++
//            startActivity(Intent(this, CreateExamActivity::class.java))
//            finish()
//        }
        dataExam = data
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onLoading(state: Boolean) {
        when(state){
            true->pg_loading.visibility=View.VISIBLE
            else->pg_loading.visibility=View.GONE
        }
    }

    override fun onError(msg: String) {
        logE(msg)
    }

    override fun showResult(result: List<QuizModel?>) {
        questions.clear()
        questions.addAll(result)
        dataExam.quizes.clear()
        dataExam.quizes.addAll(result)
        rvAdapter.notifyDataSetChanged()
    }

    override fun showExamData(result: ExamModel?) {
        showData(result)
    }

}