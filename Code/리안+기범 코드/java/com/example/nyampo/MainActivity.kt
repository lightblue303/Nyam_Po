package com.example.nyampo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nyampo.ui.AttendanceDialog
import com.example.nyampo.ui.ClosetDialog
import com.example.nyampo.ui.FeedDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var leafCount = 0
    private var moneyCount = 0
    private val leafTextView: TextView by lazy { findViewById(R.id.leafNumberText) }
    private val moneyTextView: TextView by lazy { findViewById(R.id.moneyNumberText) }
    private val checkIcon: ImageView by lazy { findViewById(R.id.check_icon) }

    private val mascotViews: List<ImageButton> by lazy {
        listOf(
            findViewById(R.id.imageButton_haero),
            findViewById(R.id.imageButton_tino)
        )
    }

    private val backgrounds: List<ImageView> by lazy {
        listOf(
            findViewById(R.id.background_base),
            findViewById(R.id.background_oido),
            findViewById(R.id.background_park),
            findViewById(R.id.background_wavepark),
            findViewById(R.id.background_tuk)
        )
    }

    private lateinit var prefs: SharedPreferences

    private val PREFS_NAME = "GamePrefs"
    private val KEY_LEAF = "leafCount"
    private val KEY_MONEY = "moneyCount"
    private val KEY_MASCOT = "selectedMascotIndex"
    private val KEY_BACKGROUND = "selectedBackgroundIndex"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        leafCount = prefs.getInt(KEY_LEAF, 155)
        moneyCount = prefs.getInt(KEY_MONEY, 1820)


        val savedMascotIndex = prefs.getInt(KEY_MASCOT, 0)
        changeMascot(savedMascotIndex)


        val savedBackgroundIndex = prefs.getInt(KEY_BACKGROUND, 0)
        changeBackground(savedBackgroundIndex, backgrounds)

        leafTextView.text = leafCount.toString()
        moneyTextView.text = moneyCount.toString()

        val progressBar = findViewById<ProgressBar>(R.id.ProgressBar)
        progressBar.max = 100
        progressBar.progress = 0

        val haeroButton = findViewById<ImageButton>(R.id.imageButton_haero)
        val tinoButton = findViewById<ImageButton>(R.id.imageButton_tino)

        val settingButton = findViewById<ImageButton>(R.id.imageButton_setting)
        val calendarButton = findViewById<ImageButton>(R.id.imageButton_calendar)
        val walkButton = findViewById<ImageButton>(R.id.imageButton_walk)
        val closetButton = findViewById<ImageButton>(R.id.imageButton_closet)
        val feedButton = findViewById<Button>(R.id.button_feed)
        val moneyButton = findViewById<Button>(R.id.button_money)

        // 출석 여부 체크 아이콘 초기 상태
        val attendancePrefs = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val todayKey = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(System.currentTimeMillis())
        checkIcon.visibility = if (attendancePrefs.getBoolean(todayKey, false)) View.VISIBLE else View.GONE

        //QR스캔 버튼
        val qrButton = findViewById<ImageButton>(R.id.imageButton_qr)
        qrButton.setOnClickListener {
            val intent = Intent(this, QrScannerActivity::class.java)
            startActivity(intent)
        }
        val bgIndexFromQR = intent.getIntExtra("background_index", -1)
        if (bgIndexFromQR != -1) {
            changeBackground(bgIndexFromQR, backgrounds)
        }

        haeroButton.setOnClickListener {  /* 해로 버튼 클릭 처리 */
            showFloatingHearts()
        }

        tinoButton.setOnClickListener {  /* 티노 버튼 클릭 처리 */
            showFloatingHearts()
        }

        settingButton.setOnClickListener {
            com.example.nyampo.ui.SettingDialog.show(this)
        }

        walkButton.setOnClickListener {
            if (progressBar.progress > 0) progressBar.progress -= 1
        }

        calendarButton.setOnClickListener {
            AttendanceDialog.show(this, checkIcon) {
                FeedDialog.showGetFeedPopup(this) { gained ->
                    leafCount += gained
                    leafTextView.text = leafCount.toString()
                    prefs.edit().putInt(KEY_LEAF, leafCount).apply()
                }
            }
        }

        closetButton.setOnClickListener {
            ClosetDialog.show(this, ::changeMascot, { idx -> changeBackground(idx, backgrounds) }, backgrounds)
        }

        feedButton.setOnClickListener {
            FeedDialog.showFeedPopup(this, leafCount) { newLeaf, rewardMoney, reward ->
                showFloatingHearts()
                leafCount = newLeaf
                moneyCount += rewardMoney
                leafTextView.text = leafCount.toString()
                moneyTextView.text = moneyCount.toString()
                prefs.edit().putInt(KEY_LEAF, leafCount).putInt(KEY_MONEY, moneyCount).apply()
                if (progressBar.progress < progressBar.max) progressBar.progress += 1
            }
        }

        moneyButton.setOnClickListener {
            // 시루 버튼 처리 (필요 시 구현)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun changeMascot(index: Int) {
        mascotViews.forEachIndexed { i, view ->
            if (i == index) {
                view.alpha = 0f
                view.visibility = View.VISIBLE
                view.animate().alpha(1f).setDuration(300).start()
            } else {
                view.visibility = View.GONE
            }
        }
        prefs.edit().putInt(KEY_MASCOT, index).apply()
    }

    private fun changeBackground(index: Int, allBackgrounds: List<ImageView>) {
        allBackgrounds.forEachIndexed { i, bg ->
            if (i == index) {
                bg.alpha = 0f
                bg.visibility = View.VISIBLE
                bg.animate().alpha(0.6f).setDuration(300).start()
            } else {
                bg.visibility = View.GONE
            }
        }
        prefs.edit().putInt(KEY_BACKGROUND, index).apply()
    }

    private fun showFloatingHearts() {
        val rootLayout = findViewById<ConstraintLayout>(R.id.main)

        val currentMascot = mascotViews.find { it.visibility == View.VISIBLE } ?: return

        repeat(10) {
            val heart = ImageView(this).apply {
                setImageResource(R.drawable.heart_icon)
                layoutParams = ConstraintLayout.LayoutParams(80, 80)
                setColorFilter(android.graphics.Color.RED)
            }

            val xOffset = (-150..150).random()
            val yOffset = (-80..80).random()

            heart.x = currentMascot.x + currentMascot.width / 2 - 40 + xOffset
            heart.y = currentMascot.y - 60 + yOffset

            rootLayout.addView(heart)

            val rotation = (-30..30).random().toFloat()
            val scale = Random.nextFloat() * (1.3f - 0.7f) + 0.7f

            heart.animate()
                .translationYBy(-400f)
                .alpha(0f)
                .rotationBy(rotation)
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(1800)
                .withEndAction { rootLayout.removeView(heart) }
                .start()
        }
    }

}