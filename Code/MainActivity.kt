package com.example.nyampo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var currentBackgroundIndex = 0
    private lateinit var backgrounds: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.ProgressBar)

        val background_base = findViewById<ImageView>(R.id.background_base)
        val background_oido = findViewById<ImageView>(R.id.background_oido)
        val background_park = findViewById<ImageView>(R.id.background_park)
        val background_wavepark = findViewById<ImageView>(R.id.background_wavepark)

        // 배경 이미지들을 순서대로 리스트에 저장
        backgrounds = listOf(background_base, background_oido, background_park, background_wavepark)

        val haeroButton = findViewById<ImageButton>(R.id.imageButton_haero)
        val settingButton = findViewById<ImageButton>(R.id.imageButton_setting)
        val walkButton = findViewById<ImageButton>(R.id.imageButton_walk)
        val calendarButton = findViewById<ImageButton>(R.id.imageButton_calendar)
        val checkIcon = findViewById<ImageView>(R.id.check_icon)

        val feedButton = findViewById<ImageButton>(R.id.imageButton_feed)
        val moneyButton = findViewById<ImageButton>(R.id.imageButton_money)

        progressBar.max = 100
        progressBar.progress = 0 // 숫자에 따라 바 길이 변경

        haeroButton.setOnClickListener {
            // 모든 배경 숨김
            backgrounds.forEach {
                it.clearAnimation()
                it.visibility = View.GONE
                it.alpha = 0.6f
            }

            // 다음 배경 인덱스 설정
            currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.size

            // 현재 배경만 표시하며 fade-in 애니메이션 적용
            backgrounds[currentBackgroundIndex].apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().alpha(0.6f).setDuration(500).start()
            }
        }

        settingButton.setOnClickListener {      //설정버튼 임시 이벤트. Progress bar +
            if (progressBar.progress < progressBar.max) {
                progressBar.progress += 1
            }
        }

        walkButton.setOnClickListener {      //걷기버튼 임시 이벤트. Progress bar -
            if (progressBar.progress <= progressBar.max) {
                progressBar.progress -= 1
            }
        }

        calendarButton.setOnClickListener {      //달력버튼 임시 이벤트.
            showCustomCalendarDialog()
        }

        feedButton.setOnClickListener {         // 먹이주기 임시 이벤트
            showFloatingHearts()
            if (progressBar.progress < progressBar.max) {
                progressBar.progress += 1
            }

        }

        moneyButton.setOnClickListener {         // 시루버튼 임시 이벤트

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showCustomCalendarDialog() {
        val dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_calendar, null)
        dialog.setContentView(view)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        val attendBtn = view.findViewById<Button>(R.id.button_attend)
        val resetBtn = view.findViewById<Button>(R.id.button_reset_attendance)
        val closeBtn = view.findViewById<ImageButton>(R.id.button_close_popup)
        val checkIcon = findViewById<ImageView>(R.id.check_icon)

        val prefs = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val todayMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val todayKey = dateFormat.format(Date(todayMillis))

        calendarView.date = todayMillis
        calendarView.minDate = todayMillis
        calendarView.maxDate = todayMillis
        calendarView.setOnTouchListener { _, _ -> true }

        var alreadyChecked = prefs.getBoolean(todayKey, false)

        fun updateAttendanceUI() {
            alreadyChecked = prefs.getBoolean(todayKey, false)
            if (alreadyChecked) {
                attendBtn.text = "✅ 출석 완료"
                attendBtn.isEnabled = false
                checkIcon.visibility = View.VISIBLE
            } else {
                attendBtn.text = "출석하기"
                attendBtn.isEnabled = true
                checkIcon.visibility = View.GONE
            }
        }

        updateAttendanceUI()

        attendBtn.setOnClickListener {
            prefs.edit().putBoolean(todayKey, true).apply()
            Toast.makeText(this, "출석 완료! 🎉", Toast.LENGTH_SHORT).show()
            updateAttendanceUI()
        }

        resetBtn.setOnClickListener {
            prefs.edit().clear().apply()
            Toast.makeText(this, "출석 데이터 초기화 완료", Toast.LENGTH_SHORT).show()
            updateAttendanceUI()
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showFloatingHearts() {
        val rootLayout = findViewById<ConstraintLayout>(R.id.main)
        val haero = findViewById<ImageButton>(R.id.imageButton_haero)

        val heartCount = 10

        for (i in 0 until heartCount) {
            val heart = ImageView(this)
            heart.setImageResource(R.drawable.heart_icon)
            heart.layoutParams = ConstraintLayout.LayoutParams(80, 80)
            heart.setColorFilter(android.graphics.Color.RED)

            // X, Y 퍼짐 범위 확장
            val randomXOffset = (-150..150).random()
            val randomYOffset = (-80..80).random()

            heart.x = haero.x + haero.width / 2 - 40 + randomXOffset
            heart.y = haero.y - 60 + randomYOffset

            rootLayout.addView(heart)

            val randomRotation = (-30..30).random().toFloat()
            val randomScale = Random.nextFloat() * (1.3f - 0.7f) + 0.7f

            heart.animate()
                .translationYBy(-400f)
                .alpha(0f)
                .rotationBy(randomRotation)
                .scaleX(randomScale)
                .scaleY(randomScale)
                .setDuration(1800)
                .withEndAction { rootLayout.removeView(heart) }
                .start()
        }
    }
}
