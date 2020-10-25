package com.dngwjy.formapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentScoreModel(
    @DocumentId
    var id: String,
    var studentId: String,
    var score: Int,
    var examId: String,
    var studentName: String,
    var takenAt: String
) : Parcelable {
    constructor() : this("", "", 0, "", "", "")
}