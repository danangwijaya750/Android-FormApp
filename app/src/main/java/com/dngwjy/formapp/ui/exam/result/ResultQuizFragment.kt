package com.dngwjy.formapp.ui.exam.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dngwjy.formapp.R
import com.dngwjy.formapp.ui.exam.CreateExamActivity

class ResultQuizFragment : Fragment() {
    companion object {
        fun getInstance(): ResultQuizFragment = ResultQuizFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            CreateExamActivity.fragmentPosition = 0
        }
    }

}