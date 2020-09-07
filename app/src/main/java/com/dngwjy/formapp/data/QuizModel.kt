package com.dngwjy.formapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizModel(
    var id: Int,
    var question: String,
    var choice: MutableList<String>,
    var questionType: String,
    var answer: String,
    var score: Int
) : Parcelable {
    constructor() : this(0, "", mutableListOf<String>(), "", "", 0)
}