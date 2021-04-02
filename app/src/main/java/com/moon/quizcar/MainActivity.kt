package com.moon.quizcar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moon.quizcar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.word.setOnClickListener {
            startActivity(Intent(this, WordActivity::class.java))
        }
        binding.clear.setOnClickListener {
            var pref = getSharedPreferences("quiz", MODE_PRIVATE)
            pref.edit().putInt("stage", 1).commit()
            updateStage()
        }
    }

    override fun onResume() {
        super.onResume()
        updateStage()
    }

    private fun updateStage() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        val stage = pref.getInt("stage", 1)
        binding.word.text = "stage: $stage"
    }
}