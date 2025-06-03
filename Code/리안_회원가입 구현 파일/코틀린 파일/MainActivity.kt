package com.tukorea.popup_example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로딩 화면처럼 보이도록 잠깐 테마 적용했다면 생략 가능
        // setContentView(R.layout.activity_main)

        // 바로 회원가입 화면으로 이동
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        // 메인 액티비티 종료 (뒤로가기 눌러도 안 돌아오게)
        finish()
    }
}

