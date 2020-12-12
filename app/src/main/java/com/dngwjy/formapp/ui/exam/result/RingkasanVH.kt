package com.dngwjy.formapp.ui.exam.result

import android.content.res.ColorStateList
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.ui.domain.QuizDomain
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.question_view_layout.*

class RingkasanVH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer, RvAdapter.BinderData<QuizDomain> {
    override fun bindData(data: QuizDomain, listen: (QuizDomain) -> Unit, position: Int) {
        tv_question_number.text = "${position + 1}"
        when (data.questionType) {
            "pilgan" -> {
                ll_pilgan.visibility = View.VISIBLE
                ll_isian.visibility = View.GONE
                isPilgan(data, position, listen)
            }
            "isian" -> {
                ll_pilgan.visibility = View.GONE
                ll_isian.visibility = View.VISIBLE
                isIsian(data, position, listen)
            }
        }
    }

    private fun isPilgan(data: QuizDomain, position: Int, listen: (QuizDomain) -> Unit) {
        tv_pilgan_question.text = data.question
        clearRg()
        data.choice.forEachIndexed { index, value ->
            val rdBtn = RadioButton(containerView.context)
            val tvPercent = TextView(containerView.context)
            val params =
                RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            val paramsRdBtn =
                RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            val linearLayout = ConstraintLayout(containerView.context)
            val linParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linearLayout.layoutParams = linParam
            params.setMargins(2, 10, 2, 10)
            linParam.setMargins(2, 10, 2, 10)
            rdBtn.layoutParams = paramsRdBtn
            tvPercent.layoutParams = params
            rdBtn.textSize = 20f
            tvPercent.textSize = 15f
            rdBtn.setPadding(10, 10, 10, 10)
            tvPercent.setPadding(10, 10, 10, 10)
            rdBtn.id = View.generateViewId()
            tvPercent.id = View.generateViewId()
            linearLayout.id = View.generateViewId()
            rdBtn.background =
                containerView.context.resources.getDrawable(R.drawable.rb_selector_drawable)
            rdBtn.buttonDrawable =
                containerView.context.resources.getDrawable(R.drawable.rb_selector_drawable)
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
            tvPercent.setTextColor(myList)
            rdBtn.text = value
            tvPercent.text = data.choiceAvg[index]
            if (value == data.answer) {
                rdBtn.isChecked = true
                tvPercent.setTextColor(containerView.context.resources.getColor(R.color.colorWhite))
            }
            rdBtn.isEnabled = false
            rg_option.addView(linearLayout)
            linearLayout.addView(rdBtn)
            linearLayout.addView(tvPercent)
            val set = ConstraintSet()
            set.clone(linearLayout)
            set.connect(tvPercent.id, ConstraintSet.END, linearLayout.id, ConstraintSet.END)
            set.connect(tvPercent.id, ConstraintSet.TOP, linearLayout.id, ConstraintSet.TOP)
            set.connect(tvPercent.id, ConstraintSet.BOTTOM, linearLayout.id, ConstraintSet.BOTTOM)
            set.applyTo(linearLayout)
        }
    }

    private fun clearRg() {
       rg_option.removeAllViewsInLayout()
    }

    private fun isIsian(data: QuizDomain, position: Int, listen: (QuizDomain) -> Unit) {
        tv_isian_question.text = data.question
    }

}