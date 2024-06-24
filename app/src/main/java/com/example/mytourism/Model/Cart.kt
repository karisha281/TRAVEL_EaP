package com.example.mytourism.Model

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.CartAdapter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Cart(private val applicationContext: Context) {
    private val sharedPreferences = applicationContext.getSharedPreferences("cart", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    fun addTourToCart(tour: Tour) {
        val tours = getTours().toMutableList()
        // Проверяем, есть ли тур с таким же id в корзине
        if (tours.none { it.id == tour.id }) {
            tours.add(tour)
            saveTours(tours)
        }
    }

    fun removeTourFromCart(tour: Tour) {
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

    private var addedTours: MutableList<Tour> = mutableListOf()

    fun setupRecyclerView(recyclerView: RecyclerView) {
        val tours = getTours()
        recyclerView.adapter = CartAdapter(tours, addedTours) { tour ->
            removeTourFromCart(tour)
        }
    }
}