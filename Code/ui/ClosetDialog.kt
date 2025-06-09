package com.example.nyampo.ui

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.example.nyampo.R

object ClosetDialog {
    fun show(
        context: Context,
        mascotChanger: (Int) -> Unit,
        backgroundChanger: (Int) -> Unit,
        backgroundViews: List<ImageView>
    ) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_closet, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnMascot = view.findViewById<Button>(R.id.btn_change_mascot)
        val btnBackground = view.findViewById<Button>(R.id.btn_change_background)

        val mascotContainer1 = view.findViewById<View>(R.id.closet_mascot_container_1)
        val mascotContainer2 = view.findViewById<View>(R.id.closet_mascot_container_2)
        val backgroundContainer1 = view.findViewById<View>(R.id.closet_background_container_1)
        val backgroundContainer2 = view.findViewById<View>(R.id.closet_background_container_2)

        val updateView = { isMascot: Boolean ->
            mascotContainer1.visibility = if (isMascot) View.VISIBLE else View.GONE
            mascotContainer2.visibility = if (isMascot) View.VISIBLE else View.GONE
            backgroundContainer1.visibility = if (!isMascot) View.VISIBLE else View.GONE
            backgroundContainer2.visibility = if (!isMascot) View.VISIBLE else View.GONE
        }

        btnMascot.setOnClickListener { updateView(true) }
        btnBackground.setOnClickListener { updateView(false) }

        listOf(
            view.findViewById<ImageButton>(R.id.imageButtonHaero),
            view.findViewById<ImageButton>(R.id.imageButtonTino)
        ).forEachIndexed { index, button ->
            button.setOnClickListener {
                mascotChanger(index)
            }
        }

        listOf(
            view.findViewById<ImageButton>(R.id.imageButtonBase),
            view.findViewById<ImageButton>(R.id.imageButtonOdio),
            view.findViewById<ImageButton>(R.id.imageButtonPark),
            view.findViewById<ImageButton>(R.id.imageButtonWavepark),
            view.findViewById<ImageButton>(R.id.imageButtonTuk)
        ).forEachIndexed { index, button ->
            button.setOnClickListener {
                backgroundChanger(index)
            }
        }

        dialog.show()
    }
}
