package com.dngwjy.formapp.ui.exam.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.model.AnswerModel
import com.dngwjy.formapp.ui.domain.QuizDomain
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_result.*

class ResultQuizFragment : Fragment() {
    companion object {
        fun getInstance(): ResultQuizFragment = ResultQuizFragment()
        var selectedIndex = "Ringkasan"
    }

    private val db = FirebaseFirestore.getInstance()

    private val listResponse = mutableListOf<AnswerModel?>()
    private val listQuizAvg = mutableListOf<QuizDomain>()
    private val listQuizResposnse = mutableListOf<QuizDomain>()

    private val listCategory = mutableListOf<String>("Ringkasan", "Tiap Soal", "Individual")
    private val rvCategoryAdapter =
        object : RvAdapter<String>(listCategory, { handleClickCategory(it) }) {
            override fun layoutId(position: Int, obj: String): Int = R.layout.item_category_rekap
            override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
                RecapCategoryVH(view)
        }

    private val rvRingkasanAdapter = object : RvAdapter<QuizDomain>(listQuizAvg, {}) {
        override fun layoutId(position: Int, obj: QuizDomain): Int = R.layout.question_view

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
            RingkasanVH(view)

    }
    private val rvTiapSoalAdapter = object : RvAdapter<QuizDomain>(listQuizResposnse, {}) {
        override fun layoutId(position: Int, obj: QuizDomain): Int = R.layout.question_quiz_layout

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
            TiapSoalVH(view)
    }

    private var responses = 0

    private fun handleClickCategory(data: String) {
        selectedIndex = data
        rvCategoryAdapter.notifyDataSetChanged()
        changeView()
    }

    private fun changeView() {
        when (selectedIndex) {
            "Ringkasan" -> {
                rv_response.apply {
                    adapter = rvRingkasanAdapter
                    val layMan = LinearLayoutManager(this@ResultQuizFragment.context)
                    layMan.orientation = LinearLayoutManager.VERTICAL
                    layoutManager = layMan
                }
                rvRingkasanAdapter.notifyDataSetChanged()
            }
            "Tiap Soal" -> {
                rv_response.apply {
                    adapter = rvTiapSoalAdapter
                    val layMan = LinearLayoutManager(this@ResultQuizFragment.context)
                    layMan.orientation = LinearLayoutManager.VERTICAL
                    layoutManager = layMan
                }
                rvTiapSoalAdapter.notifyDataSetChanged()

            }
            "Individual" -> {
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_categories.apply {
            val layManager = LinearLayoutManager(this@ResultQuizFragment.context!!)
            layManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = layManager
            adapter = rvCategoryAdapter
        }
        rvCategoryAdapter.notifyDataSetChanged()
        changeView()
        getResponse()
    }

    private fun getResponse() {
        listResponse.clear()
        db.collection("col_exam_attempt")
            .whereEqualTo("examId", CreateExamActivity.examId)
            .get()
            .addOnFailureListener {
                logE(it.localizedMessage)
            }
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    logE(it.documents.size.toString())
                    it.documents.forEach { doc ->
                        listResponse.add(doc.toObject(AnswerModel::class.java))
                    }
                    distinctData()
                    showRingkasan()
                }
            }
    }

    private fun showRingkasan() {
        CreateExamActivity.questionList.forEach { quiz ->
            listQuizAvg.add(
                QuizDomain(
                    quiz?.id!!,
                    quiz.question,
                    quiz.choice,
                    quiz.questionType,
                    quiz.answer,
                    quiz.score,
                    mutableListOf()
                )
            )
            listQuizResposnse.add(
                QuizDomain(
                    quiz.id, quiz.question, quiz.choice, quiz.questionType, quiz.answer, quiz.score,
                    mutableListOf()
                )
            )
            quiz.choice.forEach { choice ->
                val allResp = listResponse.filter { resp -> resp!!.answer == choice }.size
                val divideBy = listResponse.count { pred -> pred?.questionId == quiz.id }
                //logE("$allResp $divideBy")
                val percent = (allResp.toDouble() / divideBy.toDouble() * 100.0)
                //logE("percent $choice $percent %")
                listQuizAvg.last().choiceAvg.add("${percent}%")
                listQuizResposnse.last().choiceAvg.add("$allResp Tanggapan")
            }
        }
        when (selectedIndex) {
            "Ringkasan" -> {
                rvRingkasanAdapter.notifyDataSetChanged()
            }
            "Tiap Soal" -> {
                rvTiapSoalAdapter.notifyDataSetChanged()
            }
            "Individual" -> {
            }
        }
    }

    private fun distinctData() {
        val responseDistinct = listResponse.distinctBy { it!!.studentId }
        logE("response ${listResponse.size}")
        logE("response1 ${responseDistinct.size}")
        responses = responseDistinct.size
        tv_response.text = responses.toString()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            CreateExamActivity.fragmentPosition = 3
        }
    }

}