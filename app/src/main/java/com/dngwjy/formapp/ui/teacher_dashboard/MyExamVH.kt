package com.dngwjy.formapp.ui.teacher_dashboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.ExamModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_my_exam.*

class MyExamVH(override val containerView: View) : RecyclerView.ViewHolder(containerView)
    , LayoutContainer, RvAdapter.BinderData<ExamModel> {
    override fun bindData(data: ExamModel, listen: (ExamModel) -> Unit, position: Int) {
        if (data.id != "add-control") {
            tv_exam_title.text = data.title
            Glide.with(containerView).load(data.image)
                .placeholder(R.drawable.ic_baseline_library_books_24)
                .into(iv_exam_image)
        }

        itemView.setOnClickListener { listen(data) }
    }
}