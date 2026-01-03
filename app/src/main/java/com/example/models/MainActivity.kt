package com.example.models

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.models.DataManager
import com.example.models.MenuActivity
import com.example.racinggame1.R
import com.example.racinggame1.models.Score



class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var main_IMG_car: ImageView
    private lateinit var main_LBL_distance: TextView
    private lateinit var main_BTN_left: View
    private lateinit var main_BTN_right: View
    private lateinit var main_layout: ConstraintLayout
    private lateinit var main_IMG_hearts: Array<ImageView>

    private lateinit var sensorManager: SensorManager
    private var accSensor: Sensor? = null
    private var isSensorMode = false
    private var lastTimestamp: Long = 0
    private var gameSpeed: Long = 2000L

    private var currentLane = 2
    private var distance = 0
    private var score = 0
    private var lives = 3
    private var isGameOver = false

    private lateinit var lanePositions: FloatArray
    private val gameHandler = android.os.Handler(android.os.Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        initLanePositions()

        isSensorMode = intent.getBooleanExtra("USE_SENSORS", false)
        gameSpeed = intent.getLongExtra("GAME_SPEED", 2000L)

        if (isSensorMode) {
            initSensors()
            main_BTN_left.visibility = View.GONE
            main_BTN_right.visibility = View.GONE
        } else {
            main_BTN_left.setOnClickListener { moveCar(-1) }
            main_BTN_right.setOnClickListener { moveCar(1) }
        }

        updateCarUI()
        startGameLoop()
    }

    private fun playCrashSound() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.crash)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mp ->
            mp.release()
        }
    }

    private fun findViews() {
        main_IMG_car = findViewById(R.id.car)
        main_LBL_distance = findViewById(R.id.main_LBL_distance)
        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_layout = findViewById(R.id.main_layout_id)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )
    }

    private fun initLanePositions() {
        val screenWidth = resources.displayMetrics.widthPixels
        val laneWidth = screenWidth / 5
        val carWidthPx = (85 * resources.displayMetrics.density)
        lanePositions = FloatArray(5) { i ->
            (laneWidth * i + laneWidth / 2).toFloat() - (carWidthPx / 2)
        }
    }

    private fun startGameLoop() {
        gameHandler.post(object : Runnable {
            override fun run() {
                if (isGameOver) return
                distance += 1
                main_LBL_distance.text = "Distance: ${distance}m | Score: $score"
                spawnItem()
                gameHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun spawnItem() {
        if (isGameOver) return
        val isCoin = (1..10).random() > 7
        val item = ImageView(this)
        if (isCoin) {
            item.setImageResource(R.drawable.coin)
            item.tag = "COIN"
        } else {
            item.setImageResource(R.drawable.coal)
            item.tag = "OBSTACLE"
        }
        val size = (70 * resources.displayMetrics.density).toInt()
        item.layoutParams = ViewGroup.LayoutParams(size, size)
        val randomLane = (0..4).random()
        item.x = lanePositions[randomLane]
        item.y = -size.toFloat()
        main_layout.addView(item)
        item.animate()
            .translationY(resources.displayMetrics.heightPixels.toFloat() + 100)
            .setDuration(gameSpeed)
            .setUpdateListener { checkCollision(item) }
            .withEndAction { main_layout.removeView(item) }
            .start()
    }

    private fun checkCollision(item: ImageView) {
        if (isGameOver) return

        val carRect = Rect()
        main_IMG_car.getGlobalVisibleRect(carRect)
        val itemRect = Rect()
        item.getGlobalVisibleRect(itemRect)

        if (Rect.intersects(carRect, itemRect)) {
            if (item.tag == "COIN") {
                score += 10
                main_layout.removeView(item)
                item.clearAnimation()
            } else if (item.tag == "OBSTACLE") {
                item.visibility = View.GONE
                item.clearAnimation()
                main_layout.removeView(item)
                playCrashSound()
                handleCrash()
            }
        }
    }

    private fun handleCrash() {
        vibrate()
        if (lives > 0) {
            lives--
            if (lives < main_IMG_hearts.size) {
                main_IMG_hearts[lives].visibility = View.INVISIBLE
            }
        }
        if (lives <= 0) {
            gameOver()
        }
    }

    private fun gameOver() {
        isGameOver = true
        gameHandler.removeCallbacksAndMessages(null)


        val dataManager = DataManager(this)

        val scoreObject = Score(score = score, lat = 32.0853, lng = 34.7818)
        dataManager.saveScore(scoreObject)


        android.widget.Toast.makeText(this, "Game Over! Score: $score", android.widget.Toast.LENGTH_LONG).show()

        val intent = Intent(this, MenuActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun vibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun moveCar(direction: Int) {
        val nextLane = currentLane + direction
        if (nextLane in 0..4) {
            currentLane = nextLane
            updateCarUI()
        }
    }

    private fun updateCarUI() {
        main_IMG_car.animate().x(lanePositions[currentLane]).setDuration(100).start()
    }

    private fun initSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isSensorMode || isGameOver) return
        event?.let {
            val x = it.values[0]
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTimestamp > 350) {
                if (x < -3.0) { moveCar(1); lastTimestamp = currentTime }
                else if (x > 3.0) { moveCar(-1); lastTimestamp = currentTime }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        if (isSensorMode && ::sensorManager.isInitialized) {
            accSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (::sensorManager.isInitialized) {
            sensorManager.unregisterListener(this)
        }
    }
}