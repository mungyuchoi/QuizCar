package com.moon.quizcar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.moon.quizcar.databinding.ActivityLevelBinding

class LevelActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLevelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        val level = pref.getInt("level", 1)

        when(level) {
            1-> {
                binding.one.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.two.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.three.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.four.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.five.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.six.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.nine.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
            }
            2-> {
                binding.two.setImageResource(R.drawable.ic_two)
                binding.one.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.two.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.three.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.four.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.five.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.six.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.nine.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
            }
            3-> {
                binding.two.setImageResource(R.drawable.ic_two)
                binding.three.setImageResource(R.drawable.ic_three)
                binding.one.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.two.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.three.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.four.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.five.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.six.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.nine.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
            }
            4-> {
                binding.two.setImageResource(R.drawable.ic_two)
                binding.three.setImageResource(R.drawable.ic_three)
                binding.four.setImageResource(R.drawable.ic_four)
                binding.one.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.two.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.three.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.four.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.five.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.six.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.nine.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
            }
            5 -> {
                binding.two.setImageResource(R.drawable.ic_two)
                binding.three.setImageResource(R.drawable.ic_three)
                binding.four.setImageResource(R.drawable.ic_four)
                binding.five.setImageResource(R.drawable.ic_five)
                binding.one.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.two.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.three.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.four.setOnClickListener {
                    startActivity(Intent(this, WordActivity::class.java))
                }
                binding.five.setOnClickListener {
                    Toast.makeText(this, "다음 레벨을 준비중!", Toast.LENGTH_SHORT).show()
                }
                binding.six.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.seven.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
                binding.nine.setOnClickListener {
                    Toast.makeText(this, "전 단계를 완료하세요!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

// TODO
/*

문규야 다음 작업은 워드액티비티 실행할때 몇번 선택했는지 값을 넘기고
그 값을 받아서 그 값까지만 실행해야한다.
레벨은 마지막레벨만 기억하므로
워드에서 선택한 번호로만 임시로 * 10해서 진행해야한다
20210523 0330
 */
