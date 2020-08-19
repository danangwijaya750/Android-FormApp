package com.dngwjy.formapp.data

data class QuizModel(
    var id:Int,
    var question:String,
    var choice:MutableList<String>,
    var questionType:String,
    var answer:String,
    var score:Int
)