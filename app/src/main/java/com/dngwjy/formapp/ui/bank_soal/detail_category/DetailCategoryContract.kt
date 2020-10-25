package com.dngwjy.formapp.ui.bank_soal.detail_category

import com.dngwjy.formapp.data.model.ExamModel
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore

class DetailCategoryPresenter(
    private val db: FirebaseFirestore,
    private val view: DetailCategoryView
) {

    fun getData(category: String) {
        db.collection("col_exam")
            .whereEqualTo("category", category)
            .whereEqualTo("accessType", "Publik")
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    val result = mutableListOf<ExamModel?>()
                    it.documents.forEach { doc ->
                        result.add(doc.toObject(ExamModel::class.java))
                    }
                    view.showResult(result)
                }
            }
            .addOnFailureListener {
                logE(it.localizedMessage)
            }
    }

    fun getMyExamData(uid: String) {
        db.collection("col_exam")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    val result = mutableListOf<ExamModel?>()
                    it.documents.forEach { doc ->
                        result.add(doc.toObject(ExamModel::class.java))
                    }
                    view.showResult(result)
                }
            }
            .addOnFailureListener {
                logE(it.localizedMessage)
            }
    }


}

interface DetailCategoryView {
    fun onLoading(state: Boolean)
    fun onError(msg: String)
    fun showResult(result: List<ExamModel?>)
}