package com.dngwjy.formapp.ui.exam.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.QuizModel
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import kotlinx.android.synthetic.main.fragment_review.*

class ReviewExamFragment : Fragment() {

    companion object {
        fun getInstance(): ReviewExamFragment = ReviewExamFragment()
        val rvAdapter = object : RvAdapter<QuizModel>(CreateExamActivity.questionList, {

        }) {
            override fun layoutId(position: Int, obj: QuizModel): Int = R.layout.question_view_layout

            override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
                ViewQuizVH(view)
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_review_soal.apply {
            adapter = rvAdapter
            val layMan = LinearLayoutManager(this@ReviewExamFragment.context!!)
            layMan.orientation = LinearLayoutManager.VERTICAL
            layoutManager = layMan
        }
        rvAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        rvAdapter.notifyDataSetChanged()
    }

    public fun refreshData(){
        rvAdapter.notifyDataSetChanged()
    }


}