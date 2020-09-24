package com.dngwjy.formapp.ui.bank_soal.category

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.base.RvAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_categories.*


class CategoriesVH(override val containerView: View) : RecyclerView.ViewHolder(containerView)
    , LayoutContainer, RvAdapter.BinderData<String> {
    override fun bindData(data: String, listen: (String) -> Unit, position: Int) {
        tv_category_title.text=data
        itemView.setOnClickListener { listen(data) }
    }
}