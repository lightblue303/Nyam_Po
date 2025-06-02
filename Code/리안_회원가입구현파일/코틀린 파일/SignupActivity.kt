package com.tukorea.popup_example

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        // 툴바 설정
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "회원가입"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 상태바 패딩 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 중복 확인 버튼 동작
        val checkIdButton = findViewById<Button>(R.id.buttonCheckId)
        val editTextId = findViewById<EditText>(R.id.editTextId)

        checkIdButton.setOnClickListener {
            val enteredId = editTextId.text.toString()
            val duplicatedIds = listOf("admin", "test", "user123") // 중복 예시

            if (enteredId in duplicatedIds) {
                showOverlapPopup(editTextId)
            } else {
                showAvailablePopup(editTextId)
            }
        }
    }

    // 중복된 ID 팝업 (닫기 시 ID 삭제)
    private fun showOverlapPopup(editTextId: EditText) {
        val popupView = layoutInflater.inflate(R.layout.popup_checkid_overlap, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val closeBtn = popupView.findViewById<Button>(R.id.popup_close_btn)
        closeBtn.setOnClickListener {
            editTextId.setText("") // ID 초기화
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

    // 사용 가능한 ID 팝업
    private fun showAvailablePopup(editTextId: EditText) {
        val popupView = layoutInflater.inflate(R.layout.popup_checkid_use, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val useBtn = popupView.findViewById<Button>(R.id.popup_use_btn)
        val closeBtn = popupView.findViewById<Button>(R.id.popup_close_btn)

        // '사용' 버튼 → 입력 유지
        useBtn.setOnClickListener {
            popupWindow.dismiss()
        }

        // '닫기' 버튼 → 입력 삭제
        closeBtn.setOnClickListener {
            editTextId.setText("") // ID 초기화
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

    // 툴바 뒤로가기 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
