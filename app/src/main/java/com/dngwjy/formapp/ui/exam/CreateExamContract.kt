package com.dngwjy.formapp.ui.exam

import com.dngwjy.formapp.data.QuizModel
import com.google.firebase.firestore.FirebaseFirestore

class CreateExamPresenter(private val db: FirebaseFirestore, private val view: CreateExamView) {

    fun uploadExam(
        examName: String,
        examType: String,
        examDesc: String,
        imageUrl: String,
        accessType: String,
        accessCode: String,
        startDate: String,
        endDate: String
    ) {
        view.isLoading(true)
        val data = hashMapOf<String, Any>(
            "title" to examName,
            "desc" to examDesc,
            "category" to examType,
            "image" to imageUrl,
            "puzzles" to 0,
            "played" to 0,
            "rating" to 0.0,
            "accessType" to accessType,
            "accessCode" to accessCode,
            "startDate" to startDate,
            "endDate" to endDate
        )
        db.collection("col_exam")
            .add(data)
            .addOnSuccessListener {
                view.showUploadExamResult(it.id)
                view.isLoading(false)
            }
            .addOnFailureListener {
                view.isError(it.localizedMessage)
                view.isLoading(false)
            }
    }

}

interface CreateExamView {
    fun isLoading(state: Boolean)
    fun isError(msg: String)
    fun showUploadExamResult(data: String)
    fun showUploadQuestionResult(data: QuizModel?)
}