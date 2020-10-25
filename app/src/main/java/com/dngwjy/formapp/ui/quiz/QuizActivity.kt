package com.dngwjy.formapp.ui.quiz

import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.local.SharedPref
import com.dngwjy.formapp.data.model.AnswerModel
import com.dngwjy.formapp.data.model.ExamModel
import com.dngwjy.formapp.data.model.QuizModel
import com.dngwjy.formapp.util.convertDate
import com.dngwjy.formapp.util.logE
import com.dngwjy.formapp.util.toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
    companion object {
        val dataAnswer = mutableListOf<AnswerModel>()
        var idExam = ""
        var itemPosition = 0
        var uploadPosition = 0
    }

    private val dataQuiz = mutableListOf<QuizModel?>()
    private var scoreGotten = 0
    private var quizMaxScore = 0
    private val db = FirebaseFirestore.getInstance()
    private val quizAdapter = object : RvAdapter<QuizModel?>
        (dataQuiz, {
        toNextPosition(it)
    }) {
        override fun layoutId(position: Int, obj: QuizModel?): Int = R.layout.question_quiz_layout

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = QuizVH(view)

    }
    private var studentName = ""
    private var examAttemptId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_quiz)
        supportActionBar?.hide()
        val layManager = LinearLayoutManager(this@QuizActivity)
        layManager.orientation = LinearLayoutManager.VERTICAL
        rv_quiz.apply {
            adapter = quizAdapter
            layoutManager = layManager
        }
        studentName = SharedPref(this).userName

        showData()
    }

    private fun showSubmitButton() {
        btn_submit.visibility = View.VISIBLE
        btn_submit.setOnClickListener {
            showFinishDialog()
        }
    }

    private fun toNextPosition(data: QuizModel?) {
        checkAnswer(data)
        if (itemPosition < dataQuiz.size) {
            rv_quiz.smoothScrollToPosition(itemPosition + 1)
        }
        if (itemPosition == (dataQuiz.size - 1)) {
            showSubmitButton()
        }
    }

    private fun checkAnswer(data: QuizModel?) {
        if (data!!.answer == dataAnswer.last().answer) {
            scoreGotten += data.score
            dataAnswer.last().result = true
        }
        logE(dataAnswer.last().result.toString())

    }

    private fun uploadAttempt() {
        pg_loading.visibility = View.VISIBLE
        if (uploadPosition < dataAnswer.size) {
            doUpload()
        } else {
            pg_loading.visibility = View.GONE
            toast("Jawaban berhasil dikirim")
            showMessage()
            dataAnswer.clear()
            dataQuiz.clear()
        }
    }

    private fun doUpload() {
        val data = hashMapOf<String, Any>(
            "questionId" to dataAnswer[uploadPosition].questionId,
            "examId" to dataAnswer[uploadPosition].examId,
            "studentId" to dataAnswer[uploadPosition].studentId,
            "quizData" to dataAnswer[uploadPosition].quizData,
            "answer" to dataAnswer[uploadPosition].answer,
            "result" to dataAnswer[uploadPosition].result,
            "studentName" to studentName,
            "examAttemptScoreId" to examAttemptId
        )
        db.collection("col_exam_attempt")
            .add(data)
            .addOnSuccessListener {
                uploadPosition++
                uploadAttempt()
            }
            .addOnFailureListener {
                logE(it.message)
                toast("Terjadi Kesalahan")
            }
    }

    private fun showFinishDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val alertLayout = layoutInflater.inflate(R.layout.layout_dialog_finish_quiz, null)
        alertDialogBuilder.setView(alertLayout)
        alertDialogBuilder.setCancelable(false)
        val btnCancel = alertLayout.findViewById<Button>(R.id.btn_batal)
        val btnSubmit = alertLayout.findViewById<Button>(R.id.btn_submit)
        val dialog = alertDialogBuilder.create()
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnSubmit.setOnClickListener {
            //uploadAttempt()
            uploadScore()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showMessage() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val alertLayout = layoutInflater.inflate(R.layout.layout_succes_quiz, null)
        alertDialogBuilder.setView(alertLayout)
        alertDialogBuilder.setCancelable(false)
        val btnCancel = alertLayout.findViewById<ImageView>(R.id.btn_close)
        val btnSubmit = alertLayout.findViewById<Button>(R.id.btn_success_dialog)
        val dialog = alertDialogBuilder.create()
        btnCancel.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        btnSubmit.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }

    private fun uploadScore() {
        val data = hashMapOf<String, Any>(
            "studentId" to SharedPref(this).uid,
            "examId" to idExam,
            "score" to scoreGotten,
            "takenAt" to convertDate(System.currentTimeMillis(), "dd-MM-yyyy hh:mm:ss"),
            "studentName" to studentName
        )
        db.collection("col_student_score")
            .add(data)
            .addOnSuccessListener {
                examAttemptId = it.id
                uploadAttempt()
            }
            .addOnFailureListener {
                logE(it.localizedMessage)
            }

    }


    private fun showData() {
        val examData = intent.extras!!.get("data-quiz") as ExamModel?
        idExam = examData!!.id
        quizMaxScore = examData.quizes.sumBy { it!!.score }
        logE(quizMaxScore.toString())
        tv_exam_name.text = examData.title
        dataQuiz.clear()
        dataQuiz.addAll(examData.quizes)
        quizAdapter.notifyDataSetChanged()
    }
}