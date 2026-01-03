package com.example.models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.racinggame1.R
import com.example.racinggame1.ScoreAdapter
import com.example.racinggame1.models.Score


class ListFragment : Fragment() {

    private lateinit var main_LST_scores: RecyclerView


    interface OnScoreItemClicked {
        fun onScoreClicked(lat: Double, lng: Double)
    }

    var onScoreItemClicked: OnScoreItemClicked? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        findViews(view)
        initViews()
        return view
    }

    private fun findViews(view: View) {
        main_LST_scores = view.findViewById(R.id.main_LST_scores)
    }

    private fun initViews() {
        val dataManager = DataManager(requireContext())
        val scores = dataManager.readScores()

        val adapter = ScoreAdapter(scores, object : ScoreAdapter.ScoreCallback {
            override fun onScoreClicked(score: Score) {
                onScoreItemClicked?.onScoreClicked(score.lat, score.lng)
            }
        })

        main_LST_scores.layoutManager = LinearLayoutManager(context)
        main_LST_scores.adapter = adapter
    }
}