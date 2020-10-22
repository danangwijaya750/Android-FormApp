package com.dngwjy.formapp.data.model

import com.google.firebase.firestore.DocumentId

data class AnswerModel(
    @DocumentId
    var id: String,
    var questionId: Int,
    var examId: String,
    var studentId: String,
    var quizData: QuizModel,
    var answer: String,
    var result: Boolean
) {
    constructor() : this("", 0, "", "", QuizModel(), "", false)
}
