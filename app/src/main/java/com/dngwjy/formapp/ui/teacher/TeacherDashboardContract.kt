package com.dngwjy.formapp.ui.teacher

import com.dngwjy.formapp.data.model.ExamModel
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore

class TeacherDashboardPresenter(
    private val db: FirebaseFirestore,
    private val view: TeacherDashboardView
) {

    fun getData() {
        view.isLoading(true)
        db.collection("col_exam")
            .whereEqualTo("accessType", "Publik")
            .limit(5)
            .get()
            .addOnFailureListener {
                logE(it.localizedMessage)
                view.isError(it.localizedMessage)
                view.isLoading(false)
            }
            .addOnSuccessListener {
                val data = mutableListOf<ExamModel?>()
                data.clear()
                logE(it.documents.size.toString())
                if (!it.isEmpty) {
                    it.documents.forEach { doc ->
                        data.add(doc.toObject(ExamModel::class.java))
                    }
                    view.showResult(data)
                }
                view.isLoading(false)
            }
    }
    fun getMyExam(uid:String) {
        view.isLoading(true)
        db.collection("col_exam")
            .whereEqualTo("uid",uid)
            .limit(5)
            .get()
            .addOnFailureListener {
                logE(it.localizedMessage)
                view.isError(it.localizedMessage)
                view.isLoading(false)
            }
            .addOnSuccessListener {
                val data = mutableListOf<ExamModel?>()
                data.clear()
                logE(it.documents.size.toString())
                if (!it.isEmpty) {
                    it.documents.forEach { doc ->
                        data.add(doc.toObject(ExamModel::class.java))
                    }
                    view.showMyExam(data)
                }
                view.isLoading(false)
            }
    }
}

interface TeacherDashboardView {
    fun isLoading(state: Boolean)
    fun isError(msg: String)
    fun showResult(data: List<ExamModel?>)
    fun showMyExam(data: List<ExamModel?>)
}