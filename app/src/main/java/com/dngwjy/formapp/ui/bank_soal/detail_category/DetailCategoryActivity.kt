package com.dngwjy.formapp.ui.bank_soal.detail_category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.local.SharedPref
import com.dngwjy.formapp.data.model.ExamModel
import com.dngwjy.formapp.ui.bank_soal.detail_bank_soal.DetailBankSoalActivity
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.util.logE
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_category.*

class DetailCategoryActivity : AppCompatActivity(), DetailCategoryView {

    private val presenter = DetailCategoryPresenter(FirebaseFirestore.getInstance(), this)
    private val listExam = mutableListOf<ExamModel?>()
    private val rvAdapter = object : RvAdapter<ExamModel?>(listExam, {
        handleClick(it)
    }) {
        override fun layoutId(position: Int, obj: ExamModel?): Int = R.layout.item_exam

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = ExamVH(view)
    }

    private fun handleClick(data: ExamModel?) {
        if(intent.hasExtra("my-exam")){
            val intent = Intent(this, CreateExamActivity::class.java)
            intent.putExtra("data-exam", data)
            startActivity(intent)
        }else {
            val openIntent = Intent(this, DetailBankSoalActivity::class.java)
            openIntent.putExtra("data-exam", data)
            if (intent.hasExtra("caller")) {
                openIntent.putExtra("caller", "add")
            }
            startActivity(openIntent)
            if (intent.hasExtra("caller")) {
                finish()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_detail_category)
        supportActionBar?.hide()
        val category = intent.getStringExtra("category")
        tv_category_title.text = category
        rv_exam.apply {
            adapter = rvAdapter
            val layManager = GridLayoutManager(this@DetailCategoryActivity, 2)
            val itemOffDecor = ItemOffsetDecoration(this@DetailCategoryActivity, R.dimen.item_decor)
            addItemDecoration(itemOffDecor)
            layoutManager = layManager
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }
        //populateData(category)
        if (intent.hasExtra("category")) {
            presenter.getData(category)
        } else if (intent.hasExtra("search")) {
            if (SharedPref(this).userRole == "teacher") {
                presenter.searchData(intent.getStringExtra("search"), "Publik")
            } else {
                presenter.searchData(intent.getStringExtra("search"), "Private")
            }
        } else {
            presenter.getMyExamData(SharedPref(this).uid)
        }
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (SharedPref(this).userRole == "teacher") {
                    presenter.searchData(et_search.text.toString(), "Publik")
                } else {
                    presenter.searchData(et_search.text.toString(), "Private")
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onLoading(state: Boolean) {
        when (state) {
            true -> pg_loading.visibility = View.VISIBLE
            else -> pg_loading.visibility = View.GONE
        }
    }

    override fun onError(msg: String) {
        logE(msg)
    }

    override fun showResult(result: List<ExamModel?>) {
        listExam.clear()
        listExam.addAll(result)
        rvAdapter.notifyDataSetChanged()
    }
}