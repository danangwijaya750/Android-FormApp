package com.dngwjy.formapp.ui.exam

import com.dngwjy.formapp.data.ExamModel
import com.dngwjy.formapp.data.QuizModel
import com.google.firebase.firestore.FirebaseFirestore

class CreateExamPresenter(private val db: FirebaseFirestore, private val view: CreateExamView) {

    fun uploadExam(examName: String, examType: String, examDesc: String) {

    }

}

interface CreateExamView {
    fun isLoading(state: Boolean)
    fun isError(msg: String)
    fun showUploadExamResult(data: ExamModel?)
    fun showUploadQuestionResult(data: QuizModel?)
}