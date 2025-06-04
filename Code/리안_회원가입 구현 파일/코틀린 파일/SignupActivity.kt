package com.tukorea.popup_example

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MenuItem
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignupActivity : AppCompatActivity() {

    private var isIdChecked = false
    private var isPhoneChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "회원가입"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextId = findViewById<EditText>(R.id.editTextId)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextPasswordConfirm = findViewById<EditText>(R.id.editTextPasswordConfirm)
        val editTextPhone = findViewById<EditText>(R.id.editTextPhone)
        val editTextNickname = findViewById<EditText>(R.id.editTextNickname)
        val warningText = findViewById<TextView>(R.id.textPasswordWarning)

        val checkIdButton = findViewById<Button>(R.id.buttonCheckId)
        val checkPhoneButton = findViewById<Button>(R.id.buttonCheckPhone)
        val signupButton = findViewById<Button>(R.id.buttonSignup)

        editTextId.setOnKeyListener { _, _, _ ->
            isIdChecked = false
            false
        }

        editTextPhone.setOnKeyListener { _, _, _ ->
            isPhoneChecked = false
            false
        }

        checkIdButton.setOnClickListener {
            val enteredId = editTextId.text.toString()
            if (!isValidIdFormat(enteredId)) {
                showInvalidIdPopup(editTextId)
                isIdChecked = false
            } else {
                showAvailablePopup()
                isIdChecked = true
            }
        }

        checkPhoneButton.setOnClickListener {
            val phone = editTextPhone.text.toString()

            if (phone.length != 11) {
                showPopup(R.layout.popup_phonenum_invalid) { editTextPhone.setText("") }
                isPhoneChecked = false
            } else {
                showPopup(R.layout.popup_phonenum_valid) {}
                isPhoneChecked = true
            }
        }

        signupButton.setOnClickListener {
            val id = editTextId.text.toString()
            val pw = editTextPassword.text.toString()
            val pwConfirm = editTextPasswordConfirm.text.toString()
            val phone = editTextPhone.text.toString()
            val nickname = editTextNickname.text.toString()

            if (id.isBlank() || pw.isBlank() || pwConfirm.isBlank() || phone.isBlank() || nickname.isBlank()) {
                Toast.makeText(this, "비어있는 칸을 확인해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isIdChecked) {
                Toast.makeText(this, "아이디 중복 확인을 해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isPhoneChecked) {
                Toast.makeText(this, "전화번호 중복 확인을 해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(pw)) {
                warningText.visibility = TextView.VISIBLE
                Toast.makeText(this, "비밀번호 조건을 확인해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                warningText.visibility = TextView.GONE
            }

            if (pw != pwConfirm) {
                Toast.makeText(this, "비밀번호 확인이 다릅니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nickname.length > 8) {
                Toast.makeText(this, "닉네임은 8자 이내로 작성해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }
    }

    private fun isValidIdFormat(id: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9]{5,}$")
        return regex.matches(id)
    }

    private fun isValidPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        return regex.matches(password)
    }

    private fun showPopup(layoutResId: Int, onClose: () -> Unit) {
        val popupView = layoutInflater.inflate(layoutResId, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val closeBtn = popupView.findViewById<Button>(R.id.popup_close_btn)
        closeBtn?.setOnClickListener {
            popupWindow.dismiss()
            onClose()
        }

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

    @SuppressLint("InflateParams")
    private fun showAvailablePopup() {
        val popupView = layoutInflater.inflate(R.layout.popup_checkid_valid, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val useBtn = popupView.findViewById<Button>(R.id.popup_use_btn)

        useBtn.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

    private fun showInvalidIdPopup(editTextId: EditText) {
        showPopup(R.layout.popup_checkid_invalid) {
            editTextId.setText("")
        }
    }

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
