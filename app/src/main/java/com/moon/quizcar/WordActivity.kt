package com.moon.quizcar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.moon.quizcar.data.Item
import com.moon.quizcar.databinding.ActivityWordBinding
import kotlinx.android.synthetic.main.activity_word.view.*
import java.util.*
import kotlin.collections.ArrayList

class WordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordBinding
    private lateinit var rewardedAd: RewardedAd
    private lateinit var timerTask: Timer

    private var stage = 1

    private var lastStage = 1

    private var heart = 3

    private val list = ArrayList<Item>()

    private var time = 0

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
        binding.toolbar.findViewById<TextView>(R.id.gold).run {
            var pref = getSharedPreferences("quiz", MODE_PRIVATE)
            text = pref.getInt("gold", 200).toString()
        }

        nextStage()
        initButton()
        initResult()
        initAds()

        start()
    }

    private fun initAds() {
        MobileAds.initialize(this) {}
        binding.adView.run {
            loadAd(AdRequest.Builder().build())
        }

        rewardedAd = RewardedAd(
            this,
            "ca-app-pub-3940256099942544/5224354917"
        )
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                super.onRewardedAdLoaded()
                binding.adsVideo.isEnabled = true
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError?) {
                super.onRewardedAdFailedToLoad(adError)
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
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

    private fun initResult() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        binding.continueGame.setOnClickListener {
            if (pref.getInt("gold", 200) < 300) {
                Toast.makeText(
                    this,
                    "Gold 가 부족하여 계속 할 수 없습니다.\n충전 후 이용 부탁 드립니다.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            var gold = pref.getInt("gold", 200)
            gold -= 300
            pref.edit().putInt("gold", gold).commit()
            binding.toolbar.findViewById<TextView>(R.id.gold).run {
                text = gold.toString()
            }

            binding.wordResult.visibility = View.INVISIBLE
            binding.board.visibility = View.VISIBLE
            heart = 4
            updateHeart()
            start()
        }
        binding.retry.setOnClickListener {
            stage = 1
            nextStage()
            binding.wordResult.visibility = View.INVISIBLE
            binding.board.visibility = View.VISIBLE
            heart = 4
            updateHeart()
            time = 0
            start()
        }
        binding.adsVideo.setOnClickListener {
            if (rewardedAd.isLoaded) {
                val adCallback = object : RewardedAdCallback() {
                    override fun onRewardedAdOpened() {
                        // Ad opened.
                    }

                    override fun onRewardedAdClosed() {
                        // Ad closed.
                        rewardedAd = createAndLoadRewardedAd()
                    }

                    override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                        // User earned reward.
                        Log.i("MQ!", "onUserEarnedRewards")
                        var gold = pref.getInt("gold", 200)
                        gold += 290
                        binding.toolbar.findViewById<TextView>(R.id.gold).run {
                            text = gold.toString()
                        }
                        pref.edit().putInt("gold", gold).commit()
                        Toast.makeText(this@WordActivity, "290점이 추가 되었습니다.", Toast.LENGTH_SHORT)
                            .show()

                    }

                    override fun onRewardedAdFailedToShow(adError: AdError) {
                        // Ad failed to display.
                    }
                }
                rewardedAd.show(this, adCallback)
            } else {
                Toast.makeText(this, "아직 광고 준비가 되지 않았습니다. \n로딩이 된 후에 선택 해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun createAndLoadRewardedAd(): RewardedAd {
        val rewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
                binding.adsVideo.isEnabled = true
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                // Ad failed to load.
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
        return rewardedAd
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
                binding.toolbar.findViewById<TextView>(R.id.gold).run {
                    var pref = getSharedPreferences("quiz", MODE_PRIVATE)
                    val value = pref.getInt("gold", 200) + 10
                    text = value.toString()
                    pref.edit().putInt("gold", value).commit()
                }
                if (stage == list.size) {
                    // Result화면으로 변경
                    binding.board.visibility = View.INVISIBLE
                    binding.wordResult.visibility = View.VISIBLE
                    binding.result.text = "결과: $stage\n최고: $lastStage"
                    binding.continueGame.text = "순위 올리기"
                    binding.continueGame.setOnClickListener {
                        // TODO startActivity RankActivity
                        // AlertDialog (name), score(time)
                    }
                    binding.description.text = "축하합니다! 모두 풀었습니다!"
                    pause()
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
        time += 10
        when (heart) {
            0 -> {
                binding.toolbar.findViewById<ImageView>(R.id.heart_1)
                    .setImageDrawable(getDrawable(R.drawable.ic_no_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_2)
                    .setImageDrawable(getDrawable(R.drawable.ic_no_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_3)
                    .setImageDrawable(getDrawable(R.drawable.ic_no_heart))

                // Result화면으로 변경
                binding.board.visibility = View.INVISIBLE
                binding.wordResult.visibility = View.VISIBLE
                binding.result.text = "결과: $stage\n최고: $lastStage"
                pause()
            }
            1 -> {
                binding.toolbar.findViewById<ImageView>(R.id.heart_1)
                    .setImageDrawable(getDrawable(R.drawable.ic_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_2)
                    .setImageDrawable(getDrawable(R.drawable.ic_no_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_3)
                    .setImageDrawable(getDrawable(R.drawable.ic_no_heart))
            }
            2 -> {
                binding.toolbar.findViewById<ImageView>(R.id.heart_1)
                    .setImageDrawable(getDrawable(R.drawable.ic_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_2)
                    .setImageDrawable(getDrawable(R.drawable.ic_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_3)
                    .setImageDrawable(getDrawable(R.drawable.ic_no_heart))
            }
            3 -> {
                binding.toolbar.findViewById<ImageView>(R.id.heart_1)
                    .setImageDrawable(getDrawable(R.drawable.ic_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_2)
                    .setImageDrawable(getDrawable(R.drawable.ic_heart))
                binding.toolbar.findViewById<ImageView>(R.id.heart_3)
                    .setImageDrawable(getDrawable(R.drawable.ic_heart))
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
                    var pref = getSharedPreferences("quiz", MODE_PRIVATE)
                    lastStage = pref.getInt("stage", 1)
                    if (lastStage < stage) {
                        lastStage = stage
                        pref.edit().putInt("stage", lastStage).commit()
                    }
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

    private fun start() {
        timerTask = kotlin.concurrent.timer(period = 1000) {
            time++
            runOnUiThread {
                binding.score.text = time.toString()
            }
        }
    }

    private fun pause() {
        timerTask.cancel()
    }
}