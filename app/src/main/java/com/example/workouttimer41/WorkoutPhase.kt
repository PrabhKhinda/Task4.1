package com.example.workouttimer41

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class WorkoutPhase : AppCompatActivity() {
    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning: Boolean = false
    private var remainingTimeMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_phase)

        val timer: TextView = findViewById(R.id.wktimer)
        val pbar: ProgressBar = findViewById(R.id.progressbar)
        val stbtn: Button = findViewById(R.id.wkstop_btn)

        fun stopTimer() {
            countDownTimer?.cancel()
            pbar.progress = 0
            stbtn.isEnabled = false
            TimerAttributes.min = "00:00"
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fun startTimer(minutes: Int) {
            stbtn.isEnabled = true
            pbar.max = minutes * 60
            pbar.progress = pbar.max
            pbar.visibility = ProgressBar.VISIBLE

            val milliseconds = minutes * 60 * 1000L // Convert minutes to milliseconds
            val halfwayMillis = milliseconds / 2

            countDownTimer = object : CountDownTimer(milliseconds, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    pbar.progress = secondsRemaining.toInt()
                    val minutesRemaining = secondsRemaining / 60
                    val secondsDisplay = secondsRemaining % 60
                    val timeLeft = String.format("%02d:%02d", minutesRemaining, secondsDisplay)
                    runOnUiThread {
                        timer.text = timeLeft
                    }

                    if (!TimerAttributes.isRestPhase && millisUntilFinished <= halfwayMillis) {
                        TimerAttributes.isRestPhase = true
                        countDownTimer?.cancel()

                        val intent = Intent(this@WorkoutPhase, RestPhase::class.java)
                        startActivity(intent)
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    pbar.visibility = ProgressBar.GONE
                    stbtn.text = "Home"
                    timer.text = "Workout Completed"
                    timer.textSize = 28F
                }
            }

            if (isTimerRunning) {
                countDownTimer?.start()
            }
        }

        val minutes = TimerAttributes.min.toIntOrNull()
        if (minutes != null) {
            if (savedInstanceState != null) {
                isTimerRunning = savedInstanceState.getBoolean("isTimerRunning", false)
                remainingTimeMillis = savedInstanceState.getLong("remainingTimeMillis", 0)
            }

            if (isTimerRunning) {
                startTimer((remainingTimeMillis / 1000).toInt())
            } else {
                startTimer(minutes)
            }
        }

        stbtn.setOnClickListener {
            stopTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (!TimerAttributes.isRestPhase) {
            countDownTimer?.start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isTimerRunning", isTimerRunning)
        outState.putLong("remainingTimeMillis", countDownTimer?.let { remainingTimeMillis } ?:0 )}

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isTimerRunning = savedInstanceState.getBoolean("isTimerRunning", false)
        remainingTimeMillis = savedInstanceState.getLong("remainingTimeMillis", 0)
    }
}