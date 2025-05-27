package com.example.npexample

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        progressBar.max = 100
        progressBar.progress = 0 // 숫자에 따라 바 길이 변경

        haeroButton.setOnClickListener {
            // 모든 배경 숨김
            backgrounds.forEach { it.visibility = ImageView.GONE }

            // 다음 배경 인덱스 설정
            currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.size

            // 현재 배경만 표시
            backgrounds[currentBackgroundIndex].visibility = ImageView.VISIBLE
        }

        settingButton.setOnClickListener {      //설정버튼 임시 이벤트. Progress bar +
            if (progressBar.progress < progressBar.max) {
                progressBar.progress += 1
            }
        }

        walkButton.setOnClickListener {      //걷기버튼 임시 이벤트. Progress bar -
            if (progressBar.progress < progressBar.max) {
                progressBar.progress -= 1
            }
        }

        calendarButton.setOnClickListener {      //달력버튼 임시 이벤트.
            checkIcon.visibility =
                if (checkIcon.visibility == ImageView.VISIBLE)
                    ImageView.GONE
                else ImageView.VISIBLE
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
