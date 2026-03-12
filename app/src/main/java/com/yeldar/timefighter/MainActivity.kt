package com.yeldar.timefighter

import android.os.Bundle
import android.os.CountDownTimer
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonTapMe.setOnClickListener { incrementScore() }

        resetGame()
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }

        score++

        val newScore = getString(R.string.your_score, score)
        binding.textGameScore.text = newScore
    }

    private fun resetGame() {
        score = 0

        val initialScore = getString(R.string.your_score, score)
        binding.textGameScore.text = initialScore

        val initialTimeLeft = getString(R.string.time_left, timeLeft)
        binding.textTimeLeft.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onFinish() {
                endGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()

                val timeLeftString = getString(R.string.time_left, timeLeft)
                binding.textTimeLeft.text = timeLeftString
            }
        }

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
}