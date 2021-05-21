package com.moon.quizcar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.moon.quizcar.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var billingManager: BillingManager

    private val otherAdapter = OtherAdapter(this, arrayListOf())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        billingManager = BillingManager(this)

        FirebaseDatabase.getInstance().reference.child("Icon").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.i("MQ!","onCancelled:$error")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<Other>()
                    for (snap in snapshot.children) {
                        val info = snap.getValue(Other::class.java)
                        Log.i("MQ!", "info:$info")
                        list.add(info!!)
                    }
                    Log.i("MQ!", "list:$list")
                    otherAdapter.setItems(list)
                }
            }
        )


        binding.word.setOnClickListener {
//            startActivity(Intent(this, WordActivity::class.java))
            startActivity(Intent(this, StageActivity::class.java))
        }

        binding.otherGameImage.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                val view = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.dialog_other, null, false).apply {
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview).apply {
                            setHasFixedSize(true)
                            layoutManager = GridLayoutManager(this@MainActivity, 2)
                            this.adapter = otherAdapter
                        }

                    }
                setView(view)
                setTitle("다른 게임")
                setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }
        binding.otherGame.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                val view = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.dialog_other, null, false).apply {
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview).apply {
                            setHasFixedSize(true)
                            layoutManager = GridLayoutManager(this@MainActivity, 2)
                            this.adapter = otherAdapter
                        }

                    }
                setView(view)
                setTitle("다른 게임")
                setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
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
                setTitle("포인트 구입")
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

        binding.share.setOnClickListener {
            var shareBody = "https://play.google.com/store/apps/details?id=$packageName"
            var intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            }
            startActivity(Intent.createChooser(intent, "국산차 퀴즈 앱 공유"))
        }

        MobileAds.initialize(this, "ca-app-pub-3578188838033823~6163546889")
        binding.adView.run {
            loadAd(AdRequest.Builder().build())
        }
    }

    override fun onResume() {
        super.onResume()
        updateStage()
        updateGold()
    }

    fun updateGold() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        binding.gold.text = pref.getInt("gold", 200).toString()
    }

    private fun updateStage() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        val stage = pref.getInt("stage", 1)
        binding.stage.text = "내 전적: $stage"
    }
}

class OtherAdapter(private val context: Context, private val items: List<Other>?) :
    RecyclerView.Adapter<OtherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.other_item_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items == null) {
            return
        }
        items[position].run {
            holder.title.text = title
            Glide.with(context).load(this.imageUrl)
                .into(holder.thumbnail)
            holder.title.setOnClickListener {
                startActivity(context,
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    ), null
                )
            }
            holder.thumbnail.setOnClickListener {
                startActivity(context,
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    ), null
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    fun setItems(list: List<Other>) {
        (items as ArrayList).run {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        var title: TextView = view.findViewById(R.id.title)
    }
}

data class Other(
    val imageUrl: String = "",
    val title: String = "",
    val packageName: String = ""
)