package com.dngwjy.formapp.ui.bank_soal.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.ui.bank_soal.detail_category.DetailCategoryActivity
import kotlinx.android.synthetic.main.activity_category_bank_soal.*

class CategoryBankSoalActivity : AppCompatActivity() {
    private val categories = mutableListOf<String>()
    private val rvAdapter= object: RvAdapter<String>(categories,{
        handleClick(it)
    }){
        override fun layoutId(position: Int, obj: String): Int =R.layout.item_categories

        override fun viewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
            CategoriesVH(view)
    }
    private fun handleClick(data:String){
        val intent= Intent(this,DetailCategoryActivity::class.java)
        intent.putExtra("category",data)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_category_bank_soal)
        supportActionBar?.hide()

        rv_categories.apply{
            adapter=rvAdapter
            val layManager=LinearLayoutManager(this@CategoryBankSoalActivity)
            layManager.orientation=LinearLayoutManager.VERTICAL
            layoutManager=layManager
        }

        populateCategory()

    }

    private fun populateCategory(){
        resources.getStringArray(R.array.kategori).forEach {
            categories.add(it)
        }
        rvAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}