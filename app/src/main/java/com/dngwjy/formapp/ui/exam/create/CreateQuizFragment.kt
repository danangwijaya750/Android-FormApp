package com.dngwjy.formapp.ui.exam.create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.model.QuizModel
import com.dngwjy.formapp.ui.bank_soal.detail_category.DetailCategoryActivity
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.ui.exam.review.ReviewExamFragment
import com.dngwjy.formapp.util.logE
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.synthetic.main.fragment_create.*


class CreateQuizFragment : Fragment() {

    companion object {
        fun getInstance(): CreateQuizFragment = CreateQuizFragment()
        var questionCount = 0
        val rvAdapter =
            object : RvAdapter<QuizModel?>(CreateExamActivity.questionList, {
                updateItems()
            }) {
                override fun layoutId(position: Int, obj: QuizModel?): Int =
                    R.layout.question_layout

                override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
                    QuizVH(view)
            }

        fun updateItems() {
            CreateExamActivity.questionList.forEach {
                logE("data ${it!!.id}, ${it.question}, ${it.questionType}")
            }
            rvAdapter.notifyDataSetChanged()
            ReviewExamFragment.rvAdapter.notifyDataSetChanged()
        }
    }

    var callback: OnChangedFragmentListener? = null

    fun setOnHeadlineSelectedListener(callback: OnChangedFragmentListener?) {
        this.callback = callback
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_quiz.apply {
            adapter = rvAdapter
            val layManager = LinearLayoutManager(this@CreateQuizFragment.context)
            layManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layManager
        }
        val ballon = Balloon.Builder(context!!)
            .setLayout(R.layout.add_question_layout)
            .setArrowSize(10)
            .setArrowOrientation(ArrowOrientation.TOP)
            .setWidthRatio(0f)
            .setHeight(169)
            .setCornerRadius(4f)
            .setArrowPosition(0.5f)
            .setLifecycleOwner(this)
            .setBalloonAnimation(BalloonAnimation.CIRCULAR)
            .build()
        ll_add.setOnClickListener {
            ballon.showAlignBottom(it)
        }
        ballon.getContentView().findViewById<LinearLayout>(R.id.ll_pilgan)
            .setOnClickListener {
                addQuestions("pilgan")
                ballon.dismiss()
            }
        ballon.getContentView().findViewById<LinearLayout>(R.id.ll_isian)
            .setOnClickListener {
                addQuestions("isian")
                ballon.dismiss()
            }
        ballon.getContentView().findViewById<LinearLayout>(R.id.ll_import)
            .setOnClickListener {
                val intent = Intent(context!!, DetailCategoryActivity::class.java)
                intent.putExtra("category", CreateExamActivity.kategori)
                intent.putExtra("caller", "add")
                if (CreateExamActivity.uploaded) {
                    questionCount = CreateExamActivity.questionList.last()!!.id + 1
                }
                startActivity(intent)
            }
    }

    override fun onResume() {
        super.onResume()
        rvAdapter.notifyDataSetChanged()
    }


    private fun addQuestions(type: String) {
        logE(type)
        if (CreateExamActivity.uploaded) {
            questionCount = CreateExamActivity.questionList.last()!!.id + 1
        }
        CreateExamActivity.questionList.add(
            QuizModel(
                "a",
                questionCount, "Pertanyaan...",
                mutableListOf("Pilihan Jawaban", "Pilihan Jawaban"), type, "", 10
            )
        )
        questionCount++
        CreateExamActivity.questionList.sortBy { it!!.id }
        rvAdapter.notifyDataSetChanged()
        ReviewExamFragment.rvAdapter.notifyDataSetChanged()
        CreateExamActivity.totalScore = CreateExamActivity.questionList.sumBy { it!!.score }
        logE("Score : ${CreateExamActivity.totalScore}")
        callback!!.onChanged()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            CreateExamActivity.fragmentPosition = 0
        }
    }

    interface OnChangedFragmentListener {
        fun onChanged()
    }
}