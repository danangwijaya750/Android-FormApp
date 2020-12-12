package com.dngwjy.formapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class QuizModel(
    @Exclude
    @DocumentId
    var docId: String,
    var id: Int,
    var question: String,
    var choice: MutableList<String>,
    var questionType: String,
    var answer: String,
    var score: Int
) : Parcelable {
    constructor() : this("", 0, "", mutableListOf<String>(), "", "", 0)
}