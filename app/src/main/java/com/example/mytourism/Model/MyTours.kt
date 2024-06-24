package com.example.mytourism.Model

import android.content.Context
import com.example.mytourism.Model.Tour
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MyTours(private val applicationContext: Context) {
    private val sharedPreferences = applicationContext.getSharedPreferences("my_tours", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    fun addTourToMyTours(tour: Tour) {
        val tours = getTours().toMutableList()
        // Проверяем, есть ли тур с таким же id в списке "Мои туры"
        if (tours.none { it.id == tour.id }) {
            tours.add(tour)
            saveTours(tours)
        }
    }

    fun removeTourFromMyTours(tour: Tour) {
        val tours = getTours().toMutableList()
        tours.removeAll { it.id == tour.id }
        saveTours(tours)
    }

    fun getTours(): List<Tour> {
        val toursJson = sharedPreferences.getString("tours", "[]")
        return try {
            gson.fromJson(toursJson, object : TypeToken<List<Tour>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveTours(tours: List<Tour>) {
        val toursJson = gson.toJson(tours)
        editor.putString("tours", toursJson)
        editor.apply()
    }
}