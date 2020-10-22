package com.dngwjy.formapp.ui.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizDomain(
    var id: Int,
    var question: String,
    var choice: MutableList<String>,
    var questionType: String,
    var answer: String,
    var score: Int,
    var choiceAvg: MutableList<String>
) : Parcelable {
    constructor() : this(0, "", mutableListOf<String>(), "", "", 0, mutableListOf())
}