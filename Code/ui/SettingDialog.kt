package com.example.nyampo.ui

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import com.example.nyampo.R

object SettingDialog {
    fun show(context: Context) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_setting, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val buttonAccount = view.findViewById<Button>(R.id.button_account)
        val buttonAttend = view.findViewById<Button>(R.id.button_sound)
        val buttonLogout = view.findViewById<Button>(R.id.button_logout)

        buttonAccount.setOnClickListener {
            // 계정 설정 클릭 시 동작 (예: 토스트 띄우기 등)
        }

        buttonAttend.setOnClickListener {
            // 소리 설정 클릭 시 동작
        }

        buttonLogout.setOnClickListener {
            // 로그아웃 클릭 시 동작
        }

        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
}
