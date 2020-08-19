package com.dngwjy.formapp.ui.bank_soal.category

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.ExamModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_categories.*
import kotlinx.android.synthetic.main.item_populer_exam.*


class CategoriesVH(override val containerView: View) : RecyclerView.ViewHolder(containerView)
    , LayoutContainer, RvAdapter.BinderData<String> {
    override fun bindData(data: String, listen: (String) -> Unit, position: Int) {
        tv_category_title.text=data
        itemView.setOnClickListener { listen(data) }
    }
}