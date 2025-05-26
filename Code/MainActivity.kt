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

    private var isBackground1Visible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.ProgressBar)

        val background1 = findViewById<ImageView>(R.id.backgroundImage1)
        val background2 = findViewById<ImageView>(R.id.backgroundImage2)
        val settingButton = findViewById<ImageButton>(R.id.imageButton_setting)
        val heroButton = findViewById<ImageButton>(R.id.imageButton_hero)

        progressBar.max = 100
        progressBar.progress = 0 // 숫자에 따라 바 길이 변경

        settingButton.setOnClickListener {      //설정버튼 임시 이벤트. Progress bar +
            if (progressBar.progress < progressBar.max) {
                progressBar.progress += 1
            }
        }
        heroButton.setOnClickListener {      //해로버튼 임시 이벤트. 배경화면 변경
            if (isBackground1Visible) {
                background1.visibility = ImageView.GONE
                background2.visibility = ImageView.VISIBLE
            } else {
                background1.visibility = ImageView.VISIBLE
                background2.visibility = ImageView.GONE
            }
            isBackground1Visible = !isBackground1Visible
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
