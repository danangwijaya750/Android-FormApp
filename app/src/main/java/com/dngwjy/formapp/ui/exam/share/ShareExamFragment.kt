package com.dngwjy.formapp.ui.exam.share

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dngwjy.formapp.R
import kotlinx.android.synthetic.main.fragment_share.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ShareExamFragment : Fragment() {

    companion object {
        fun getInstance(): ShareExamFragment = ShareExamFragment()
        var score = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_mulai.text = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm").format(
            Instant.now().atZone(
                ZoneId.of("Asia/Jakarta")
            )
        )
        tv_selesai.text = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm").format(
            Instant.now()
                .atZone(ZoneId.of("Asia/Jakarta")).plusHours(1)
        )

        tv_mulai.setOnClickListener {
            pickDateTime(0)
        }
        tv_selesai.setOnClickListener {
            pickDateTime(1)
        }
    }

    private fun pickDateTime(caller: Int) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        val pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(year, month, day, hour, minute)
                        val sdf=SimpleDateFormat("dd-MM-yyyy hh:mm")
                        when (caller) {
                            0 -> tv_mulai.text = sdf.format(pickedDateTime.time)
                            1 -> tv_selesai.text = sdf.format(pickedDateTime.time)
                        }
                    },
                    startHour,
                    startMinute,
                    false
                ).show()
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }
}