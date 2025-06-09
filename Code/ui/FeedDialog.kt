package com.example.nyampo.ui

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.nyampo.R
import java.util.*

object FeedDialog {
    fun showFeedPopup(
        context: Context,
        leafCount: Int,
        onSuccess: (newLeaf: Int, newMoney: Int, reward: Int) -> Unit
    ) {
        if (leafCount <= 0) {
            Toast.makeText(context, "먹이가 부족합니다", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_feed, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val confirmBtn = view.findViewById<Button>(R.id.button_feed_confirm)
        val textView = view.findViewById<TextView>(R.id.textView)

        val reward = (10..100).random()
        textView.text = "$reward 시루 획득!"

        confirmBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            onSuccess(leafCount - 1, reward, reward)
        }

        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    fun showGetFeedPopup(context: Context, onReward: (Int) -> Unit) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_get_feed, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val confirmBtn = view.findViewById<Button>(R.id.button_feed_confirm)
        confirmBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            onReward(1)
        }

        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
}
