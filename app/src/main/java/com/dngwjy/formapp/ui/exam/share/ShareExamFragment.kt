package com.dngwjy.formapp.ui.exam.share

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dngwjy.formapp.R
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import kotlinx.android.synthetic.main.fragment_share.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class ShareExamFragment : Fragment() {

    companion object {
        fun getInstance(): ShareExamFragment = ShareExamFragment()
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
        CreateExamActivity.startDate = tv_mulai.text.toString()
        CreateExamActivity.endDate = tv_selesai.text.toString()

        tv_mulai.setOnClickListener {
            pickDateTime(0)
            CreateExamActivity.startDate = tv_mulai.text.toString()
        }
        tv_selesai.setOnClickListener {
            pickDateTime(1)
            CreateExamActivity.endDate = tv_selesai.text.toString()
        }
        iv_image_exam.setOnClickListener {
            pickImage()
        }
        sp_akses.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                CreateExamActivity.accessType = sp_akses.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                CreateExamActivity.accessType = sp_akses.selectedItem.toString()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (CreateExamActivity.bitmap != null) {
            setImage(CreateExamActivity.bitmap)
        }
    }

    private fun pickImage() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // ******** code for crop image
        i.putExtra("crop", "true")
        i.putExtra("aspectX", 100)
        i.putExtra("aspectY", 100)
        i.putExtra("outputX", 256)
        i.putExtra("outputY", 356)

        try {
            i.putExtra("return-data", true)
            startActivityForResult(
                Intent.createChooser(i, "Select Picture"), 0
            )
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                setImage(bitmap)
                CreateExamActivity.bitmap = bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setImage(bitmap: Bitmap?) {
        Glide.with(this)
            .load(bitmap)
            .into(iv_image_exam)
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
                        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm")
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            et_score.setText(CreateExamActivity.totalScore.toString())
            CreateExamActivity.fragmentPosition = 2
        }
    }
}