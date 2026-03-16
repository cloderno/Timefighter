package com.yeldar.timefighter

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yeldar.timefighter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var score = 0

    private lateinit var countDownTimer: CountDownTimer
    private var gameStarted = false
    private var initialCountDown: Long = 30000
    private var countDownInterval: Long = 1000
    private var timeLeft = 30

    private val TAG = "MainActivity"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time left: $timeLeft")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonTapMe.setOnClickListener { incrementScore() }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        Log.d(TAG, "onCreate called. Score is: $score")
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }

        score++
        setScore()
    }

    private fun restoreGame() {
        setScore()
        setTimeLeft()

        initTimer((timeLeft * 1000).toLong())

        countDownTimer.start()
        gameStarted = true
    }

    private fun resetGame() {
        score = 0

        setScore()
        initTimer(initialCountDown)
        setTimeLeft()

        gameStarted = false
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over, score), Toast.LENGTH_LONG).show()
        resetGame()
    }

    private fun initTimer(countDownTime: Long) {
        countDownTimer = object : CountDownTimer(countDownTime, countDownInterval) {
            override fun onFinish() {
                endGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()

                val timeLeftString = getString(R.string.time_left, timeLeft)
                binding.textTimeLeft.text = timeLeftString
            }
        }
    }

    private fun setScore() {
        val initialScore = getString(R.string.your_score, score)
        binding.textGameScore.text = initialScore
    }

    private fun setTimeLeft() {
        val initialTimeLeft = getString(R.string.time_left, timeLeft)
        binding.textTimeLeft.text = initialTimeLeft
    }

    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }
}