package com.dngwjy.formapp.ui.exam.result

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.base.RvAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category_rekap.*

class RecapCategoryVH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer, RvAdapter.BinderData<String> {
    override fun bindData(data: String, listen: (String) -> Unit, position: Int) {
        if (ResultQuizFragment.selectedIndex == data) {
            vi_indicator.visibility = View.VISIBLE
        } else {
            vi_indicator.visibility = View.INVISIBLE
        }
        tv_category.text = data
        itemView.setOnClickListener { listen(data) }
    }
}