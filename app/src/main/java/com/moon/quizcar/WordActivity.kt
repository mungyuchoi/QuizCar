package com.moon.quizcar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.moon.quizcar.data.Item
import com.moon.quizcar.databinding.ActivityWordBinding
import java.util.*
import kotlin.collections.ArrayList

class WordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordBinding

    private var stage = 1

    private var heart = 3

    private val list = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setContentInsetsAbsolute(0, 0)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(
                LayoutInflater.from(this@WordActivity).inflate(R.layout.word_toolbar, null),
                ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT
                )
            )
        }

        for (i in 0 until 15) {
            list.add(Item(Const().thumbnail[i], Const().name[i]))
        }
        list.shuffle()
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        stage = pref.getInt("stage", 1)

        nextStage()
        initButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initButton() {
        updateButton(binding.btn1)
        updateButton(binding.btn2)
        updateButton(binding.btn3)
        updateButton(binding.btn4)
        binding.toolbar.findViewById<ImageView>(R.id.back)?.run {
            setOnClickListener {
                finish()
            }
        }
    }

    private fun updateButton(textView: TextView) {
        textView.setOnClickListener {
            if (textView.text.toString().equals(list[stage - 1].name, ignoreCase = true)) {
                // 정답
                if (stage == list.size) {
                    finish()
                } else {
                    updateAnimate()
                    binding.toolbar.findViewById<TextView>(R.id.stage)?.run {
                        text = stage.toString()
                    }
                }
            } else {
                // 틀림
                updateHeart()
            }
        }
    }

    private fun updateHeart() {
        heart--
        when(heart) {
            0 ->{
                binding.toolbar.findViewById<ImageView>(R.id.heart_1).setImageDrawable(getDrawable(R.drawable.ic_no_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_2).setImageDrawable(getDrawable(R.drawable.ic_no_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_3).setImageDrawable(getDrawable(R.drawable.ic_no_heart))

                // Result화면으로 변경
            }
            1 -> {
                binding.toolbar.findViewById<ImageView>(R.id.heart_1).setImageDrawable(getDrawable(R.drawable.ic_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_2).setImageDrawable(getDrawable(R.drawable.ic_no_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_3).setImageDrawable(getDrawable(R.drawable.ic_no_heart))
            }
            2 -> {
                binding.toolbar.findViewById<ImageView>(R.id.heart_1).setImageDrawable(getDrawable(R.drawable.ic_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_2).setImageDrawable(getDrawable(R.drawable.ic_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_3).setImageDrawable(getDrawable(R.drawable.ic_no_heart))
            }
        }
    }

    private fun updateAnimate() {
        binding.btn1.animate().alpha(0f).translationX(-200f).setDuration(500).withEndAction {
            binding.btn1.translationX = 0f
            binding.btn1.alpha = 1f
        }.start()
        binding.btn2.animate().alpha(0f).translationX(-200f).setDuration(500).withEndAction {
            binding.btn2.translationX = 0f
            binding.btn2.alpha = 1f
        }.start()
        binding.btn3.animate().alpha(0f).translationX(-200f).setDuration(500).withEndAction {
            binding.btn3.translationX = 0f
            binding.btn3.alpha = 1f
        }.start()
        binding.btn4.animate().alpha(0f).translationX(-200f).setDuration(500).withEndAction {
            binding.btn4.translationX = 0f
            binding.btn4.alpha = 1f
        }.start()
        binding.thumbnail.animate().alpha(0f).translationX(-200f).setDuration(500)
            .withEndAction {
                binding.thumbnail.translationX = 0f
                binding.thumbnail.alpha = 1f
                if (stage < list.size) {
                    stage++
                    nextStage()
                } else {
                    Toast.makeText(this@WordActivity, "게임 끝!", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }.start()
    }

    private fun nextStage() {
        binding.toolbar.findViewById<TextView>(R.id.stage)?.run {
            text = stage.toString()
        }

        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        pref.edit().putInt("stage", stage).commit()
        binding.thumbnail.setImageResource(list[stage - 1].thumbnail)

        var random = Random()
        val correctAnswer = random.nextInt(4) + 1
        var first = stage - 1
        var second = 0
        var third = 0
        var forth = 0

        var green = resources.getColorStateList(R.color.green)
        var red = resources.getColorStateList(R.color.red)
        when (correctAnswer) {
            1 -> {
                binding.btn1.text = list[first].name
                binding.btn1.rippleColor = green
                do {
                    second = random.nextInt(list.size)
                } while (second == first)
                do {
                    third = random.nextInt(list.size)
                } while (third == first || third == second)
                do {
                    forth = random.nextInt(list.size)
                } while (forth == first || forth == second || forth == third)
                binding.btn2.text = list[second].name
                binding.btn3.text = list[third].name
                binding.btn4.text = list[forth].name
                binding.btn2.rippleColor = red
                binding.btn3.rippleColor = red
                binding.btn4.rippleColor = red
            }
            2 -> {
                binding.btn2.text = list[first].name
                binding.btn2.rippleColor = green
                do {
                    second = random.nextInt(list.size)
                } while (second == first)
                do {
                    third = random.nextInt(list.size)
                } while (third == first || third == second)
                do {
                    forth = random.nextInt(list.size)
                } while (forth == first || forth == second || forth == third)
                binding.btn1.text = list[second].name
                binding.btn3.text = list[third].name
                binding.btn4.text = list[forth].name
                binding.btn1.rippleColor = red
                binding.btn3.rippleColor = red
                binding.btn4.rippleColor = red
            }
            3 -> {
                binding.btn3.text = list[first].name
                binding.btn3.rippleColor = green
                do {
                    second = random.nextInt(list.size)
                } while (second == first)
                do {
                    third = random.nextInt(list.size)
                } while (third == first || third == second)
                do {
                    forth = random.nextInt(list.size)
                } while (forth == first || forth == second || forth == third)
                binding.btn2.text = list[second].name
                binding.btn1.text = list[third].name
                binding.btn4.text = list[forth].name
                binding.btn2.rippleColor = red
                binding.btn1.rippleColor = red
                binding.btn4.rippleColor = red
            }
            4 -> {
                binding.btn4.text = list[first].name
                binding.btn4.rippleColor = green
                do {
                    second = random.nextInt(list.size)
                } while (second == first)
                do {
                    third = random.nextInt(list.size)
                } while (third == first || third == second)
                do {
                    forth = random.nextInt(list.size)
                } while (forth == first || forth == second || forth == third)
                binding.btn2.text = list[second].name
                binding.btn3.text = list[third].name
                binding.btn1.text = list[forth].name
                binding.btn2.rippleColor = red
                binding.btn3.rippleColor = red
                binding.btn1.rippleColor = red
            }
        }

    }
}