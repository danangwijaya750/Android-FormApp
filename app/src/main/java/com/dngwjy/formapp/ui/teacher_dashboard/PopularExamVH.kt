package com.dngwjy.formapp.ui.teacher_dashboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dngwjy.formapp.R
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.ExamModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_populer_exam.*


class PopularExamVH(override val containerView: View) : RecyclerView.ViewHolder(containerView)
    , LayoutContainer, RvAdapter.BinderData<ExamModel> {
    override fun bindData(data: ExamModel, listen: (ExamModel) -> Unit, position: Int) {
        tv_exam_title.text = data.title
        if(data.image.isNotEmpty()) {
            Glide.with(containerView).load(data.image)
                .placeholder(R.drawable.ic_baseline_library_books_24)
                .into(iv_exam_image)
        }
        tv_exam_ratings.text=data.rating.toString()
        tv_exam_puzzle.text=data.puzzles.toString()
        tv_exam_play.text="${data.played}x"
        data.tags.forEach {
            tv_exam_tags.text = "${tv_exam_tags.text} $it "
        }
        itemView.setOnClickListener { listen(data) }
    }
}