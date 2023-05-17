package com.example.workouttimer41

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn:Button= findViewById(R.id.btn)
        val wk_time:EditText= findViewById(R.id.wk_time)
        val rt_time:EditText=findViewById(R.id.rt_time)

        btn.setOnClickListener{
            TimerAttributes.min=wk_time.text.toString()
            TimerAttributes.restmin=rt_time.text.toString()
            val intent = Intent(this, WorkoutPhase::class.java)

            startActivity(intent)

        }

    }
}