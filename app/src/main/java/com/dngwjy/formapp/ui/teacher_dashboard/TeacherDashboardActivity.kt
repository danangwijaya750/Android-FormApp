package com.dngwjy.formapp.ui.teacher_dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.ExamModel
import com.dngwjy.formapp.data.QuizModel
import com.dngwjy.formapp.ui.bank_soal.category.CategoryBankSoalActivity
import com.dngwjy.formapp.ui.bank_soal.detail_bank_soal.DetailBankSoalActivity
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import kotlinx.android.synthetic.main.activity_teacher_dashboard.*

class TeacherDashboardActivity : AppCompatActivity() {

    val myExamList= mutableListOf<ExamModel>()
    val popularExamList= mutableListOf<ExamModel>()

    private val myExamAdapter= object:RvAdapter<ExamModel>(myExamList,{
        rvMyExamClickHandler(it)
    }){
        override fun layoutId(position: Int, obj: ExamModel): Int = R.layout.item_my_exam
        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = MyExamVH(view)

    }

    private val popularExamAdapter= object:RvAdapter<ExamModel>(popularExamList,{
        rvPopularClickHandler(it)
    }){
        override fun layoutId(position: Int, obj: ExamModel): Int = R.layout.item_populer_exam
        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder = PopularExamVH(view)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_teacher_dashboard)
        supportActionBar?.hide()
        myExamList.add(
            0, ExamModel(
                "add-control", "", "", "", "", 0.0, mutableListOf(), 0, 0,
                mutableListOf()
            )
        )
        rv_ujian_anda.apply {
            adapter = myExamAdapter
            val layManager = LinearLayoutManager(this@TeacherDashboardActivity)
            layManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = layManager
        }
        rv_ujian_populer.apply {
            adapter = popularExamAdapter
            val layManager = LinearLayoutManager(this@TeacherDashboardActivity)
            layManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager=layManager
        }

        tv_ujian_populer_all.setOnClickListener {
            startActivity(Intent(this,CategoryBankSoalActivity::class.java))

        }

        myExamAdapter.notifyDataSetChanged()
        populatePopularExam()
    }

    private fun populatePopularExam(){
        popularExamList.add(
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
                mutableListOf(
                    QuizModel(
                        1,
                        "Paus termasuk hewan ...",
                        mutableListOf("Reptil", "Mamalia", "Insekta", "Unggas"),
                        "pilgan",
                        "Mamalia",
                        10
                    ),
                    QuizModel(
                        2, "Kambing pemakan rumput, disebut ...",
                        mutableListOf(
                            "Karnivora", "Omnivora", "Herbivora"
                        ), "pilgan", "Herbivora", 10
                    ),
                    QuizModel(
                        3, "Jumlah kaki Hiu adalah ...",
                        mutableListOf(), "isian", "0", 10
                    )
                )
            )
        )
        popularExamList.add(
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
        popularExamList.add(
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
        popularExamList.add(
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
        popularExamList.add(
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

    private fun rvMyExamClickHandler(data:ExamModel){
        if(data.id=="add-control"){
         showCreateNew()
        }
    }
    private fun rvPopularClickHandler(data:ExamModel){
        val intent=Intent(this,DetailBankSoalActivity::class.java)
        intent.putExtra("data-exam",data)
        startActivity(intent)
    }

    private fun showCreateNew(){
        val alertDialogBuilder=AlertDialog.Builder(this)
        val alertLayout=layoutInflater.inflate(R.layout.layout_create_dialog,null)
        alertDialogBuilder.setView(alertLayout)
        alertDialogBuilder.setCancelable(false)
        val etJudul= alertLayout.findViewById<EditText>(R.id.et_judul)
        val etKeterangan= alertLayout.findViewById<EditText>(R.id.et_keterangan)
        val spKategori= alertLayout.findViewById<Spinner>(R.id.sp_kategori)
        val btnCancel=alertLayout.findViewById<ImageView>(R.id.btn_close)
        val btnSubmit=alertLayout.findViewById<Button>(R.id.btn_submit)
        val dialog=alertDialogBuilder.create()
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnSubmit.setOnClickListener {
            CreateExamActivity.examTitle=etJudul.text.toString()
            CreateExamActivity.keterangan=etKeterangan.text.toString()
            CreateExamActivity.kategori=spKategori.selectedItem.toString()
            dialog.dismiss()
            startActivity(Intent(this,CreateExamActivity::class.java))
        }
        dialog.show()
    }
}