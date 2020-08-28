package com.dngwjy.formapp.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExamModel(
    @DocumentId
    var id: String,
    var image: String,
    var title: String,
    var desc: String,
    var category: String,
    var rating: Double,
    var tags: MutableList<String?>,
    var puzzles: Int,
    var played: Int,
    var quizes: MutableList<QuizModel?>
):Parcelable