package com.dngwjy.formapp.ui.exam

import com.dngwjy.formapp.data.model.QuizModel
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreateExamPresenter(private val db: FirebaseFirestore, private val view: CreateExamView) {

    fun uploadExam(
        examName: String,
        examType: String,
        examDesc: String,
        imageUrl: String,
        accessType: String,
        accessCode: String,
        startDate: String,
        endDate: String,
        image: ByteArray?
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
                if (image != null) uploadExamImage(it.id, image)
                else view.showUploadExamResult(it.id)
            }
            .addOnFailureListener {
                view.isError(it.localizedMessage)
                view.isLoading(false)
            }

    }

    private fun uploadExamImage(idExam: String, dataImage: ByteArray) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://formforexam-670e3.appspot.com")
        val imageRef = storageRef.child("images/${idExam}.jpg")
        imageRef.putBytes(dataImage)
            .addOnSuccessListener {
                updateImageUrl(idExam)
            }
            .addOnFailureListener {
                logE(it.localizedMessage)
                view.isError(it.localizedMessage)
            }
    }

    private fun updateImageUrl(idExam: String) {
        var url = when (idExam[0]) {
            '9' -> {
                "https://firebasestorage.googleapis.com/v0/b/formforexam-670e3.appspot.com/o/images%2F${idExam}.jpg?alt=media"
            }
            else -> {
                "https://firebasestorage.googleapis.com/v0/b/formforexam-670e3.appspot.com/o/images%2F9${idExam}.jpg?alt=media"
            }
        }
        db.collection("col_exam").document(idExam)
            .update(
                "image",
                url
            )
            .addOnSuccessListener {
                view.showUploadExamResult(idExam)
            }
            .addOnFailureListener {
                logE(it.localizedMessage)
                view.isError(it.localizedMessage)
            }
    }

    fun uploadQuestion(idExam: String, data: QuizModel) {
        db.collection("col_exam").document(idExam)
            .collection("questions")
            .add(data)
            .addOnSuccessListener {
                view.showUploadExamResult(idExam)
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