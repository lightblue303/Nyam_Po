package com.example.nyampo.ui

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.nyampo.R
import com.google.firebase.database.FirebaseDatabase
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

object AttendanceDialog {
    val firebaseDB = FirebaseDatabase.getInstance("https://nyampo-7d71d-default-rtdb.asia-southeast1.firebasedatabase.app")
    fun show(context: Context, userId:String, checkIcon: ImageView, onFeedReward: () -> Unit) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_calendar, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        val attendBtn = view.findViewById<Button>(R.id.button_attend)
//        val resetBtn = view.findViewById<Button>(R.id.button_reset_attendance)

        val prefs = context.getSharedPreferences("AttendancePrefs_${userId}", Context.MODE_PRIVATE)
        val todayKey = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(System.currentTimeMillis()))

        calendarView.apply {
            date = System.currentTimeMillis()
            minDate = System.currentTimeMillis()
            maxDate = System.currentTimeMillis()
            setOnTouchListener { _, _ -> true }
        }

        fun updateUI() {
            val alreadyChecked = prefs.getBoolean(todayKey, false)
            checkIcon.visibility = if (alreadyChecked) View.VISIBLE else View.GONE
            attendBtn.text = if (alreadyChecked) "✅ 출석 완료" else "출석하기"
            attendBtn.isEnabled = !alreadyChecked
        }

        updateUI()

        attendBtn.setOnClickListener {
            prefs.edit().putBoolean(todayKey, true).apply()
            updateUI()
            onFeedReward()
        }

//        resetBtn.setOnClickListener {
//            prefs.edit().clear().apply()
//            Toast.makeText(context, "출석 데이터 초기화 완료", Toast.LENGTH_SHORT).show()
//            updateUI()
//        }

        dialog.show()
    }
}