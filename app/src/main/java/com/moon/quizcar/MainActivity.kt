package com.moon.quizcar

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moon.quizcar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var billingManager: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        billingManager = BillingManager(this)

        binding.word.setOnClickListener {
            startActivity(Intent(this, WordActivity::class.java))
        }

        binding.otherGameImage.setOnClickListener {
            Toast.makeText(this, "준비중입니다!", Toast.LENGTH_SHORT).show()
        }
        binding.otherGame.setOnClickListener {
            Toast.makeText(this, "준비중입니다!", Toast.LENGTH_SHORT).show()
        }

        binding.review.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        }
        binding.reviewImage.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        }

        binding.purchase.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                var pref = getSharedPreferences("quiz", MODE_PRIVATE)
                val view = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.dialog_purchase, null, false).apply {
                        findViewById<ConstraintLayout>(R.id.thousand_layout).setOnClickListener {
                            billingManager.purchase("1000", this@MainActivity)
                            pref.edit().putInt("purchase", 1000).commit()
                        }
                        findViewById<ConstraintLayout>(R.id.five_thousand_layout).setOnClickListener {
                            billingManager.purchase("5000", this@MainActivity)
                            pref.edit().putInt("purchase", 5000).commit()
                        }
                        findViewById<ConstraintLayout>(R.id.ten_thousand_layout).setOnClickListener {
                            billingManager.purchase("10000", this@MainActivity)
                            pref.edit().putInt("purchase", 10000).commit()
                        }
                    }
                setView(view)
                setTitle("구매")
                setMessage("원하는 상품을 구매해주세요.")
                setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }
        binding.purchaseImage.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                var pref = getSharedPreferences("quiz", MODE_PRIVATE)
                val view = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.dialog_purchase, null, false).apply {
                        findViewById<ConstraintLayout>(R.id.thousand_layout).setOnClickListener {
                            billingManager.purchase("1000", this@MainActivity)
                            pref.edit().putInt("purchase", 1000).commit()
                        }
                        findViewById<ConstraintLayout>(R.id.five_thousand_layout).setOnClickListener {
                            billingManager.purchase("5000", this@MainActivity)
                            pref.edit().putInt("purchase", 5000).commit()
                        }
                        findViewById<ConstraintLayout>(R.id.ten_thousand_layout).setOnClickListener {
                            billingManager.purchase("10000", this@MainActivity)
                            pref.edit().putInt("purchase", 10000).commit()
                        }
                    }
                setView(view)
                setTitle("구매")
                setMessage("원하는 상품을 구매해주세요.")
                setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }
        
        binding.rank.setOnClickListener {
            Toast.makeText(this, "이달의 순위!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RankActivity::class.java))
        }
        binding.rankImage.setOnClickListener {
            Toast.makeText(this, "이달의 순위!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RankActivity::class.java))
        }

        updateGold()

        MobileAds.initialize(this) {}
        binding.adView.run {
            loadAd(AdRequest.Builder().build())
        }
    }

    override fun onResume() {
        super.onResume()
        updateStage()
    }

    fun updateGold() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        binding.gold.text = pref.getInt("gold", 200).toString()
    }

    private fun updateStage() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        val stage = pref.getInt("stage", 1)
        binding.stage.text = "최고단계: $stage"
    }
}