package com.dngwjy.formapp.ui.answer

import android.content.res.ColorStateList
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.model.AnswerModel
import com.dngwjy.formapp.data.model.QuizModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.question_student_view.*

class AnswerVH (override val containerView: View) : RecyclerView.ViewHolder(containerView)
    , LayoutContainer, RvAdapter.BinderData<AnswerModel?> {
    override fun bindData(data: AnswerModel?, listen: (AnswerModel?) -> Unit, position: Int) {
        tv_question_number.text="${position+1}"
        when(data!!.quizData.questionType){
            "pilgan"->{
                ll_pilgan.visibility=View.VISIBLE
                ll_isian.visibility=View.GONE
                isPilgan(data.quizData, position,data.answer)
            }
            "isian"->{
                ll_pilgan.visibility=View.GONE
                ll_isian.visibility=View.VISIBLE
                isIsian(data.quizData, position,data.answer)
            }
        }
    }
    private fun isPilgan(data: QuizModel, position: Int,answer:String) {
        tv_pilgan_question.text = data.question
        tv_key_pilgan.setText(data.answer)
        clearRg()
        data.choice.forEachIndexed { index, value ->
            val rdBtn = RadioButton(containerView.context)
            val params =
                RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            params.setMargins(2, 10, 2, 10)
            rdBtn.layoutParams = params
            rdBtn.textSize = 20f
            rdBtn.setPadding(10, 10, 10, 10)
            rdBtn.id=View.generateViewId()
            rdBtn.background=containerView.context.resources.getDrawable(R.drawable.rb_selector_drawable)
            rdBtn.buttonDrawable=containerView.context.resources.getDrawable(R.drawable.rb_selector_drawable)
            val states = arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            )

            val colors = intArrayOf(
                containerView.context.resources.getColor(R.color.colorWhite),
                containerView.context.resources.getColor(R.color.colorPrimary)
            )
            val myList = ColorStateList(states, colors)
            rdBtn.setTextColor(myList)
            rdBtn.text = value
            if (value == data.answer) rdBtn.isChecked = true
            rdBtn.isEnabled = false
            rg_option.addView(rdBtn)
        }
    }

    private fun clearRg(){
        val count: Int = rg_option.childCount
        if (count > 0) {
            for (i in count - 1 downTo 0) {
                val o: View = rg_option.getChildAt(i)
                if (o is RadioButton) {
                    rg_option.removeViewAt(i)
                }
            }
        }
    }

    private fun isIsian(data: QuizModel, position: Int,answer:String) {
        tv_key_isian.setText(data.answer)
        tv_isian_question.text = data.question
    }
}