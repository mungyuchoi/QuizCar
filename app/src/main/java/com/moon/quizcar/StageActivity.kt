package com.moon.quizcar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moon.quizcar.databinding.ActivityStageBinding

class StageActivity: AppCompatActivity() {

    private lateinit var binding: ActivityStageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStageBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
