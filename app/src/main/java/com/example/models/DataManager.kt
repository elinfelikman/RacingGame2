package com.example.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.racinggame1.models.Score

class DataManager(private val context: Context) {

    private val gson = Gson()

    fun saveScore(score: Score) {
        val scores = readScores().toMutableList()
        scores.add(score)

        val topScores = scores.sortedByDescending { it.score }.take(10)

        val json = gson.toJson(topScores)
        val sharedPreferences = context.getSharedPreferences("RACING_GAME_DB", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("SCORES", json).apply()
    }

    fun readScores(): List<Score> {
        val sharedPreferences = context.getSharedPreferences("RACING_GAME_DB", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("SCORES", null) ?: return emptyList()
        val type = object : TypeToken<List<Score>>() {}.type
        return gson.fromJson(json, type)
    }
}