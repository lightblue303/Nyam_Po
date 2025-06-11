package com.example.nyampo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // 상태바/내비게이션바 패딩 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextId = findViewById<EditText>(R.id.editTextId)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        // 회원가입 버튼 클릭
        val signupButton = findViewById<Button>(R.id.buttonSignup)
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 클릭
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            val userId = editTextId.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            when {
                userId.isEmpty() -> {
                    Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show()
                }

                password.isEmpty() -> {
                    Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // 유효성 검사 통과 → 다음 화면으로 이동
                    val intent = Intent(this, ChooseherotoroActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}