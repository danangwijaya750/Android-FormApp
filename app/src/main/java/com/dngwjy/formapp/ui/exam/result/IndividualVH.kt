package com.dngwjy.formapp.ui.exam.result

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dngwjy.formapp.base.RvAdapter
import com.dngwjy.formapp.data.model.StudentScoreModel
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.util.stringToDate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_student_score.*

class IndividualVH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer, RvAdapter.BinderData<StudentScoreModel?> {
    override fun bindData(
        data: StudentScoreModel?,
        listen: (StudentScoreModel?) -> Unit,
        position: Int
    ) {
        tv_name.text = data?.studentName
        tv_score.text = data?.score.toString()
        tv_date.text = data?.takenAt
        if (stringToDate(data?.takenAt!!, "dd-MM-yyyy hh:mm:ss").time > stringToDate(
                CreateExamActivity.endDate,
                "dd-MM-yyyy hh:mm"
            ).time
        ) {
            tv_status.visibility = View.VISIBLE
        }
        tv_selengkapnya.setOnClickListener {
            listen(data)
        }
    }
}