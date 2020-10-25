package com.dngwjy.formapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerModel(
    @DocumentId
    var id: String,
    var questionId: Int,
    var examId: String,
    var studentId: String,
    var quizData: QuizModel,
    var answer: String,
    var result: Boolean,
    var studentName:String,
    var examAttemptScoreId:String
):Parcelable {
    constructor() : this("", 0, "", "", QuizModel(), "", false,"","")
}
