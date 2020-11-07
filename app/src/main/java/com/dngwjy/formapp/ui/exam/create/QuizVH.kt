package com.dngwjy.formapp.ui.exam.create

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.model.QuizModel
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.util.logE
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.question_layout.*
import kotlinx.android.synthetic.main.question_layout.ll_isian
import kotlinx.android.synthetic.main.question_layout.ll_pilgan
import kotlinx.android.synthetic.main.question_layout.tv_question_number


class QuizVH(override val containerView: View) : RecyclerView.ViewHolder(containerView)
    , LayoutContainer, RvAdapter.BinderData<QuizModel> {
    override fun bindData(data: QuizModel, listen: (QuizModel) -> Unit, position: Int) {
        tv_question_number.text = "${position + 1}"
        et_score.text = "${data.score}"
        et_score.setOnClickListener {
            changeScore(data, listen)
        }
        bt_kunci.setOnClickListener {
          if(data.questionType=="pilgan"){
              keyPilgan(data,listen)
          }else{
              keyText(data,listen)
          }
        }
        when (data.questionType) {
            "pilgan" -> {
                ll_isian.visibility = View.GONE
                ll_pilgan.visibility = View.VISIBLE
                ll_essay.visibility = View.GONE
                isPilgan(data, position, listen)
            }
            "isian" -> {
                ll_isian.visibility = View.VISIBLE
                ll_pilgan.visibility = View.GONE
                ll_essay.visibility = View.GONE
                et_jawaban.visibility=View.GONE
                isIsian(data, position, listen)
            }
            "essay"->{
                ll_isian.visibility = View.GONE
                ll_pilgan.visibility = View.GONE
                ll_essay.visibility=View.VISIBLE
                et_jawaban.visibility=View.GONE
                isEssay(data, position, listen)
            }
        }
        iv_attach.setOnClickListener {

        }
        iv_more.setOnClickListener {

            val ballon = Balloon.Builder(containerView.context)
                .setLayout(R.layout.layout_more_dialog)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setWidthRatio(0f)
                .setHeight(110)
                .setCornerRadius(4f)
                .setArrowPosition(0.5f)
                .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                .build()
            ballon.showAlignBottom(it)
            ballon.getContentView().findViewById<LinearLayout>(R.id.ll_hapus)
                .setOnClickListener {
                    ballon.dismiss()
                    var id = 0
                    CreateExamActivity.questionList.forEachIndexed { index, it ->
                        if (it!!.id == data.id) {
                            id = index
                            CreateExamActivity.questionList.removeAt(id)
                            return@forEachIndexed
                        }
                    }
                    listen(data)
                }
            ballon.getContentView().findViewById<LinearLayout>(R.id.ll_duplikat)
                .setOnClickListener {
                    ballon.dismiss()
                    CreateQuizFragment.questionCount++
                    val id = CreateQuizFragment.questionCount
                    CreateExamActivity.questionList.add(
                        QuizModel(
                            id,
                            data.question,
                            data.choice,
                            data.questionType,
                            data.answer,
                            data.score
                        )
                    )
                    listen(data)
                }
        }
    }
    private fun keyText(data:QuizModel,listen: (QuizModel) -> Unit){
        val builder = AlertDialog.Builder(containerView.context)
        val input = EditText(containerView.context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(data.answer)
        builder.apply {
            setTitle("Ubah Kunci Jawaban")
            setView(input)
            setPositiveButton("Simpan") { dialog, which ->
                changeAnswer(input.text.toString(),data, listen)
                dialog.dismiss()
            }
            setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
                listen(data)
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun keyPilgan(data:QuizModel,listen: (QuizModel) -> Unit){
        val builder = AlertDialog.Builder(containerView.context)
        val input = RadioGroup(containerView.context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        var answer=""
        input.layoutParams = lp
        input.removeAllViewsInLayout()
        data.choice.forEach {value->
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
            rdBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    answer=value
                }
            }
            input.addView(rdBtn)
        }
        builder.apply {
            setTitle("Ubah Kunci Jawaban")
            setView(input)
            setPositiveButton("Simpan") { dialog, which ->
                changeAnswer(answer,data, listen)
                dialog.dismiss()
            }
            setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
                listen(data)
            }
        }
        val alert = builder.create()
        alert.show()
    }


    private fun isPilgan(data: QuizModel, position: Int, listen: (QuizModel) -> Unit) {
        et_pilgan_question.text = data.question
        et_pilgan_question.setOnClickListener {
            changeQuestion(data, listen)
        }

        clearRg()
        data.choice.forEachIndexed { index, value ->
            val rdBtn = EditText(containerView.context)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(2, 10, 2, 10)
            rdBtn.run {
                params.setMargins(2, 10, 2, 10)
                layoutParams = params
                setPadding(10, 10, 10, 10)
                id = View.generateViewId()
                setText(value)
                textSize = 18f
                inputType = InputType.TYPE_CLASS_TEXT
                imeOptions = EditorInfo.IME_ACTION_DONE
                setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        v: TextView?,
                        actionId: Int,
                        event: KeyEvent?
                    ): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            changging(index, text.toString(), position, data, listen)
                            return true
                        }
                        return false
                    }
                })
            }
            if(value=="Pilihan Jawaban"){
                rdBtn.setText("")
                rdBtn.hint=value
            }
//            rdBtn.setOnClickListener {
//                changeAnswer(index, value, position, data, listen)
//            }

            //rdBtn.background=containerView.context.resources.getDrawable(R.drawable.rb_selector_drawable)
//            val states = arrayOf(
//                intArrayOf(android.R.attr.state_checked),
//                intArrayOf(-android.R.attr.state_checked)
//            )
//
//            val colors = intArrayOf(
//                containerView.context.resources.getColor(R.color.colorWhite),
//                containerView.context.resources.getColor(R.color.colorPrimary)
//            )
//            val myList = ColorStateList(states, colors)
//            rdBtn.setTextColor(myList)
//            if (data.answer == value){
//
//            }
//            else{
//
//            }


            ll_option.addView(rdBtn)
        }
        tv_add_choice.setOnClickListener {
            data.choice.add("Pilihan Jawaban")
            listen(data)
        }
    }

    private fun changeScore(data: QuizModel, listen: (QuizModel) -> Unit) {
        val builder = AlertDialog.Builder(containerView.context)
        val input = EditText(containerView.context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(data.score.toString())
        builder.apply {
            setTitle("Ubah Score")
            setView(input)
            setPositiveButton("Simpan") { dialog, which ->
                var id = 0
                CreateExamActivity.questionList.forEachIndexed { index, it ->
                    if (it!!.id == data.id) {
                        id = index
                        CreateExamActivity.questionList[id]!!.score = input.text.toString().toInt()
                        return@forEachIndexed
                    }
                }
                dialog.dismiss()
                listen(data)
            }
            setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
                listen(data)
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun clearRg() {
//        val count: Int = ll_option.childCount
//        if (count > 0) {
//            for (i in count - 1 downTo 0) {
//                ll_option.remov
//
//            }
//        }
        ll_option.removeAllViewsInLayout()
    }

    private fun changeQuestion(data: QuizModel, listen: (QuizModel) -> Unit) {

        val builder = AlertDialog.Builder(containerView.context)
        val input = EditText(containerView.context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(data.question)
        if(data.question=="Masukan Pertanyaan..."){
            input.setText("")
            input.hint="Masukan Pertanyaan..."
        }
        builder.apply {
            setTitle("Ubah Pertanyaan")
            setView(input)
            setPositiveButton("Simpan") { dialog, which ->
                var id = 0
                CreateExamActivity.questionList.forEachIndexed { index, it ->
                    if (it!!.id == data.id) {
                        id = index
                        CreateExamActivity.questionList[id]!!.question = input.text.toString()
                        return@forEachIndexed
                    }
                }
                dialog.dismiss()
                listen(data)
            }
            setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
                listen(data)
            }
        }
        val alert = builder.create()
        alert.show()

    }

    private fun changging(
        index: Int,
        value: String,
        position: Int,
        data: QuizModel,
        listen: (QuizModel) -> Unit
    ) {
        var id = 0
        CreateExamActivity.questionList.forEachIndexed { idx, it ->
            if (it!!.id == data.id) {
                id = idx
                CreateExamActivity.questionList[id]!!.choice[index] = value.toString()
                return@forEachIndexed
            }
        }
        listen(data)
    }


    private fun changeOption(
        index: Int,
        value: String,
        position: Int,
        data: QuizModel,
        listen: (QuizModel) -> Unit
    ) {
        val builder = AlertDialog.Builder(containerView.context)
        val input = EditText(containerView.context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(value)
        builder.apply {
            setTitle("Ubah pilihan")
            setView(input)
            setPositiveButton("Simpan") { dialog, which ->
                var id = 0
                CreateExamActivity.questionList.forEachIndexed { idx, it ->
                    if (it!!.id == data.id) {
                        id = idx
                        CreateExamActivity.questionList[id]!!.choice[index] = input.text.toString()
                        return@forEachIndexed
                    }
                }
                dialog.dismiss()
                listen(data)
            }
            setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
                listen(data)
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun changeAnswer(
        value: String,
        data: QuizModel,
        listen: (QuizModel) -> Unit
    ) {
        var id = 0
        CreateExamActivity.questionList.forEachIndexed { idx, it ->
            if (it!!.id == data.id) {
                id = idx
                CreateExamActivity.questionList[id]!!.answer = value
                return@forEachIndexed
            }
        }
        listen(data)
        logE("data ${CreateExamActivity.questionList[id]!!.answer}")
    }

    private fun isIsian(data: QuizModel, position: Int, listen: (QuizModel) -> Unit) {
        et_isian_question.text = data.question
        et_isian_question.setOnClickListener {
            changeQuestion(data, listen)
        }
    }
    private fun isEssay(data: QuizModel, position: Int, listen: (QuizModel) -> Unit) {
        et_essay_question.text = data.question
        et_essay_question.setOnClickListener {
            changeQuestion(data, listen)
        }
    }


}