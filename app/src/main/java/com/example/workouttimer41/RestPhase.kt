package com.example.workouttimer41

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class RestPhase : AppCompatActivity() {
    var countDownTimer: CountDownTimer? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_phase)

        val timer: TextView =findViewById(R.id.wktimer)
        val pbar: ProgressBar =findViewById(R.id.progressbar)
        val stbtn: Button =findViewById(R.id.wkstop_btn)

        fun stopTimer() {
            countDownTimer?.cancel()
            TimerAttributes.isRestPhase=false
//            pbar.progress=0
//            stbtn.isEnabled = false
//            TimerAttributes.min="00:00"
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }


        fun startTimer(minutes: Int) {

            stbtn.isEnabled = true
            pbar.max = minutes * 60
            pbar.progress = pbar.max
            pbar.visibility= ProgressBar.VISIBLE

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
                }

                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    // Handle timer finished event
                    pbar.visibility = ProgressBar.GONE


                    stbtn.text="Home"
                    timer.text="Rest Phase Completed"
                    timer.textSize=28F
                    finish()
                    TimerAttributes.isRestPhase=false


                }


            }

            (countDownTimer as CountDownTimer).start()

        }




        val minutes = TimerAttributes.restmin.toIntOrNull()
        if (minutes != null) {
            startTimer(minutes)
        }


        stbtn.setOnClickListener {
            stopTimer()
        }



    }
    }
