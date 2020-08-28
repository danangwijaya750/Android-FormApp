package com.dngwjy.formapp.ui.bank_soal.detail_category

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.ExamModel
import kotlinx.android.synthetic.main.activity_detail_category.*

class DetailCategoryActivity : AppCompatActivity() {

    private val listExam= mutableListOf<ExamModel>()
    private val rvAdapter=object: RvAdapter<ExamModel>(listExam,{

    }){
        override fun layoutId(position: Int, obj: ExamModel): Int = R.layout.item_exam

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =ExamVH(view)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_detail_category)
        supportActionBar?.hide()
        val category = intent.getStringExtra("category")
        tv_category_title.text=category
        rv_exam.apply {
            adapter=rvAdapter
            val layManager=GridLayoutManager(this@DetailCategoryActivity,2)
            val itemOffDecor=ItemOffsetDecoration(this@DetailCategoryActivity,R.dimen.item_decor)
            addItemDecoration(itemOffDecor)
            layoutManager=layManager
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }
        populateData(category)
    }

    private fun populateData(category: String){
        listExam.clear()
        when(category){
            "Bahasa Indonesia"->{getBIndo()}
            "Bahasa Inggris"->{getBIng()}
            "Ilmu Pengetahuan Alam"->{getIpa()}
            "Ilmu Pengetahuan Sosial"->{getIps()}
            "Matematika"->{getMatematika()}
        }
        rvAdapter.notifyDataSetChanged()
    }

    private fun getMatematika(){
        listExam.add(
            ExamModel(
                "3",
                "",
                "Penjumlahan",
                "ini ujian",
                "Matematika",
                4.5,
                mutableListOf("#jumlah", "#tambah", "#matematika"),
                48,
                60,
                mutableListOf()
            )
        )
    }
    private fun getIps(){
        listExam.add(
            ExamModel(
                "2",
                "",
                "Interaksi Sosial",
                "ini ujian",
                "Ilmu Pengetahuan Sosial",
                4.2,
                mutableListOf("#interaksi", "#keragaman"),
                38,
                80,
                mutableListOf()
            )
        )
    }
    private fun getIpa(){
        listExam.add(
            ExamModel(
                "1",
                "",
                "Makhluk Hidup",
                "ini ujian",
                "Ilmu Pengetahuan Alam",
                4.8,
                mutableListOf("#hewan", "#tumbuhan", "#tubuh"),
                50,
                80,
                mutableListOf()
            )
        )
    }
    private fun getBIndo(){
        listExam.add(
            ExamModel(
                "4",
                "",
                "Pantun",
                "ini ujian",
                "Bahasa Indonesia",
                4.7,
                mutableListOf("#pantun", "#bindo", "#sajak"),
                41,
                76,
                mutableListOf()
            )
        )
        listExam.add(
            ExamModel(
                "5",
                "",
                "Cerpen",
                "ini ujian",
                "Bahasa Indonesia",
                4.4,
                mutableListOf("#cerita", "#pendek"),
                53,
                84,
                mutableListOf()
            )
        )
    }
    private fun getBIng(){

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}