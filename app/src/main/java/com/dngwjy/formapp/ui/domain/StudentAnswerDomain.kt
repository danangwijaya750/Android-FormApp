package com.dngwjy.formapp.ui.domain

import android.os.Parcelable
import com.dngwjy.formapp.data.model.AnswerModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentAnswerDomain(
    var id:String,
    var studentId:String,
    var score:Int,
    var examId:String,
    var studentName:String,
    var takenAt:String,
    var examName:String,
    var answers:MutableList<AnswerModel?>
):Parcelable