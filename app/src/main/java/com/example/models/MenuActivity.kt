package com.example.models

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.example.racinggame1.R

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_BTN_start_buttons: MaterialButton
    private lateinit var menu_BTN_start_sensors: MaterialButton
    private lateinit var menu_BTN_scores: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()
    }

    private fun findViews() {
        menu_BTN_start_buttons = findViewById(R.id.menu_BTN_start_buttons)
        menu_BTN_start_sensors = findViewById(R.id.menu_BTN_start_sensors)
        menu_BTN_scores = findViewById(R.id.menu_BTN_scores)
    }

    private fun initViews() {

        menu_BTN_start_buttons.setOnClickListener {
            startGame(false)
        }


        menu_BTN_start_sensors.setOnClickListener {
            startGame(true)
        }
        menu_BTN_scores.setOnClickListener {
            val intent = Intent(this, ScoreActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startGame(useSensors: Boolean) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USE_SENSORS", useSensors)


        val radioGroup = findViewById<RadioGroup>(R.id.menu_RG_speed)
        val speed = if (radioGroup.checkedRadioButtonId == R.id.menu_RB_fast) 1500L else 3000L

        intent.putExtra("GAME_SPEED", speed)
        startActivity(intent)
    }
}