package com.moon.quizcar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.moon.quizcar.databinding.ActivityRankBinding
import java.util.*

class RankActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankBinding
    private lateinit var recyclerView: RecyclerView
    private val rankAdapter = RankAdapter(arrayListOf())
    private val rankList = ArrayList<Rank>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.run {
            setHasFixedSize(true)
            this.adapter = rankAdapter
        }


        MobileAds.initialize(this) {}
        binding.adView.run {
            loadAd(AdRequest.Builder().build())
        }

        FirebaseDatabase.getInstance().reference.child("Rank")
            .child((Calendar.MONTH + 1).toString() + "월").orderByChild("score").limitToFirst(100).run {
                addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        rankList.clear()
                        rankList.add(Rank("순위", 0, "날짜"))
                        for (snap in snapshot.children) {
                            val info = snap.getValue(Rank::class.java)
                            rankList.add(info!!)
                        }
                        if (rankList.size == 0) {
                            Toast.makeText(this@RankActivity, "기록이 없습니다.", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                        rankAdapter.setItems(rankList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }

    }
}

class RankAdapter(private val items: List<Rank>?) : RecyclerView.Adapter<RankAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rank_item_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items == null) {
            return
        }
        items[position].run {
            holder.rank.text =
                if (position == 0) "Rank" else
                    position.toString()
            holder.userId.text = id
            holder.score.text = score.toString()
            holder.date.text = date
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    fun setItems(list: List<Rank>) {
        (items as ArrayList).run {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rank: TextView = view.findViewById(R.id.rank)
        var userId: TextView = view.findViewById(R.id.user_id)
        var score: TextView = view.findViewById(R.id.score)
        var date: TextView = view.findViewById(R.id.date)
    }
}


data class Rank(
    val id: String = "",
    val score: Int = 0,
    val date: String = ""
)

