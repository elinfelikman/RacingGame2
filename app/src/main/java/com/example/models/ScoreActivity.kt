package com.example.models

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.racinggame1.R

class ScoreActivity : AppCompatActivity() {

    private lateinit var listFragment: ListFragment
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)


        listFragment = ListFragment()
        mapFragment = MapFragment()


        listFragment.onScoreItemClicked = object : ListFragment.OnScoreItemClicked {
            override fun onScoreClicked(lat: Double, lng: Double) {
                mapFragment.zoomToLocation(lat, lng)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.score_FRAME_list, listFragment)
            .replace(R.id.score_FRAME_map, mapFragment)
            .commit()
    }
}