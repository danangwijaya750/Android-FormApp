package com.dngwjy.formapp.ui.exam

import com.dngwjy.formapp.data.model.QuizModel
import com.dngwjy.formapp.util.logD
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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
        image: ByteArray?,
        uid: String,
        kelas: String,
        tags: MutableList<String>
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
            "endDate" to endDate,
            "uid" to uid,
            "kelas" to kelas
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
                getDownloadURL(imageRef, idExam)
            }
            .addOnFailureListener {
                logE(it.localizedMessage)
                view.isError(it.localizedMessage)
            }
    }

    private fun getDownloadURL(imageRef: StorageReference, idExam: String) {
        imageRef.downloadUrl.addOnFailureListener {
            logE(it.localizedMessage)
        }.addOnSuccessListener {
            logD("url $it")
            updateImageUrl(idExam, it.toString())
        }
    }

    private fun updateImageUrl(idExam: String, url: String) {
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

    fun getQuizData(examId: String) {
        view.isLoading(true)
        db.collection("col_exam").document(examId)
            .collection("questions")
            .get()
            .addOnSuccessListener {
                val result = mutableListOf<QuizModel?>()
                if (!it.isEmpty) {
                    it.documents.forEach { doc ->
                        result.add(doc.toObject(QuizModel::class.java))
                    }
                    result.sortBy { attr -> attr!!.id }
                    view.showGetQuizResult(result)
                    view.isLoading(false)
                }
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
    fun showGetQuizResult(data: List<QuizModel?>)
}