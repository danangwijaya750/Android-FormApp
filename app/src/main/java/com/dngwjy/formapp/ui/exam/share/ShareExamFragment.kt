package com.dngwjy.formapp.ui.exam.share

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.dngwjy.formapp.R
import com.dngwjy.formapp.ui.exam.CreateExamActivity
import com.dngwjy.formapp.util.logE
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

        tv_mulai.setOnClickListener {
            pickDateTime(0)

        }
        tv_selesai.setOnClickListener {
            pickDateTime(1)
            CreateExamActivity.endDate = tv_selesai.text.toString()
        }
        iv_image_exam.setOnClickListener {
            pickImage()
        }
        if (CreateExamActivity.uploaded) {
            Glide.with(this.context!!)
                .asBitmap()
                .load(CreateExamActivity.imageUrl)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        setImage(resource)
                        CreateExamActivity.bitmap = resource
                    }

                })
        }
        et_tags.run {
            inputType = InputType.TYPE_CLASS_TEXT
            imeOptions = EditorInfo.IME_ACTION_DONE

            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        val tags = et_tags.text.toString().split("#").toTypedArray().toMutableList()
                        tags.removeAt(0)
                        CreateExamActivity.tags.clear()
                        CreateExamActivity.tags.addAll(tags)
                        logE("tags size ${CreateExamActivity.tags.size}")
                        return true
                    }
                    return true
                }
            })
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
                            0 -> {
                                tv_mulai.text = sdf.format(pickedDateTime.time)
                                CreateExamActivity.startDate = tv_mulai.text.toString()
                            }
                            1 -> {
                                tv_selesai.text = sdf.format(pickedDateTime.time)
                                CreateExamActivity.endDate = tv_selesai.text.toString()
                            }
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
            CreateExamActivity.fragmentPosition = 2
            CreateExamActivity.totalScore = CreateExamActivity.questionList.sumBy { it!!.score }
            et_score.setText(CreateExamActivity.totalScore.toString())
            if (!CreateExamActivity.uploaded) {
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
            } else {
                tv_mulai.text = CreateExamActivity.startDate
                tv_selesai.text = CreateExamActivity.endDate
            }

            if (CreateExamActivity.tags.size > 0) {
                et_tags.setText(
                    CreateExamActivity.tags.joinToString(
                        prefix = "#",
                        postfix = "",
                        separator = "#"
                    )
                )
            }
        }
    }
}