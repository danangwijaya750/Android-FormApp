package com.dngwjy.formapp.ui.bank_soal.detail_bank_soal

import com.dngwjy.formapp.data.model.QuizModel
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore

class DetailBankSoalPresenter(
    private val db: FirebaseFirestore,
    private val view: DetailBankSoalView
) {
    fun getData(examId: String) {
        db.collection("col_exam")
            .document(examId)
            .collection("questions")
            .get()
            .addOnFailureListener {
                logE(it.localizedMessage)
                view.onError(it.localizedMessage)
            }
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    val result = mutableListOf<QuizModel?>()
                    it.documents.forEach { doc ->
                        result.add(doc.toObject(QuizModel::class.java))
                    }
                    view.showResult(result)
                }
            }
    }
}

interface DetailBankSoalView {
    fun onLoading(state: Boolean)
    fun onError(msg: String)
    fun showResult(result: List<QuizModel?>)
}