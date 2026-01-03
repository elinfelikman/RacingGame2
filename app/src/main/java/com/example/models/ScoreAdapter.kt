package com.example.racinggame1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.racinggame1.models.Score

class ScoreAdapter(
    private val scores: List<Score>,
    private val scoreCallback: ScoreCallback
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    interface ScoreCallback {
        fun onScoreClicked(score: Score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.score_item, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val currentScore = scores[position]


        holder.scoreLabel.text = "Score: ${currentScore.score}"


        holder.itemView.setOnClickListener {
            scoreCallback.onScoreClicked(currentScore)
        }
    }

    override fun getItemCount(): Int = scores.size

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scoreLabel: TextView = itemView.findViewById(R.id.score_LBL_score)
    }
}