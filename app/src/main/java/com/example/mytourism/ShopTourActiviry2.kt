package com.example.mytourism

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.Model.Tour
import com.example.mytourism.adapter.TourAdapter
import java.time.LocalDate

class ShopTourActivity2 : AppCompatActivity(), TourAdapter.OnItemClickListener  {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TourAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_tour_activiry2)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val countryName = intent.getStringExtra("country_name")
        Log.d("ShopTourActivity2", "Country name: $countryName")

        val tours = getToursByCountry(countryName)

        adapter = TourAdapter(tours) { tour ->
            onTourDetailsClicked(tour)
        }
        recyclerView.adapter = adapter



    }

    private fun getToursByCountry(countryName: String?): List<Tour> {
        // Здесь вы должны реализовать логику получения списка туров для выбранной страны
        return when (countryName) {
            "Россия" -> getRussiaTours()
            "Турция" -> getTurkeyTours()
            "Египет" -> getEgyptTours()
            else -> emptyList()
        }
    }

    private val allTours: List<Tour> = getRussiaTours() + getTurkeyTours() + getEgyptTours()

    fun getTourById(tourId: Int): Tour? {
        return allTours.find { it.id == tourId }
    }

    private fun getRussiaTours(): List<Tour> {
        return listOf(
            Tour(
                id = 1,
                name = "Тур по Москве",
                description = "Экскурсия по главным достопримечательностям Москвы",
                price = 28000,
                currency = "RUB",
                imageResource = R.drawable.tour_in_moscow,
                days = 3,
                hotel = "Хаятт Ридженси"
            ),
            Tour(
                id = 2,
                name = "Путешествие по Золотому Кольцу",
                description = "Посещение древних городов Центральной России",
                price = 55000,
                currency = "RUB",
                imageResource = R.drawable.tour_gold,
                days = 5,
                hotel = "5-звездочные отели в каждом городе"
            )
        )
    }

    private fun getTurkeyTours(): List<Tour> {
        return listOf(
            Tour(
                id = 3,
                name = "Тур по Стамбулу",
                description = "Знакомство с историей и культурой Стамбула",
                price = 85000,
                currency = "RUB",
                imageResource = R.drawable.tour_in_stambul,
                days = 5,
                hotel = "Elite World"
            ),
            Tour(
                id = 4,
                name = "Отдых на Средиземноморье",
                description = "Пляжный отдых и посещение достопримечательностей",
                price = 107000,
                currency = "RUB",
                imageResource = R.drawable.tour_in_sredizemnomorie,
                days = 7,
                hotel = "Круизный лайнер Astoria"
            )
        )
    }

    private fun getEgyptTours(): List<Tour> {
        return listOf(
            Tour(
                id = 5,
                name = "Круиз по Нилу",
                description = "Путешествие по реке Нил с посещением храмов и древних памятников",
                price = 110000,
                currency = "RUB",
                imageResource = R.drawable.tour_in_neel,
                days = 8,
                hotel = "Круизный корабль Iberotel Crown Emperor 4 "
            ),
            Tour(
                id = 6,
                name = "Экскурсия в Каир",
                description = "Посещение Великих пирамид Гизы и Музея египетских древностей",
                price = 120000,
                currency = "RUB",
                imageResource = R.drawable.tour_in_kair,
                days = 5,
                hotel = "Dusit Thani Lakeview Cairo"
            ),
            Tour(
                id = 7,
                name = "Отдых на Красном море",
                description = "Пляжный отдых, подводное плавание и осмотр коралловых рифов",
                price = 80000,
                currency = "RUB",
                imageResource = R.drawable.tour_red_sea,
                days = 6,
                hotel = "Сьерра Шарм-Эль-Шейх"
            )
        )
    }


    override fun onTourDetailsClicked(tour: Tour) {
        val intent = Intent(this, TourDetailsActivity::class.java)
        intent.putExtra("tour", tour)
        startActivity(intent)
    }


    }
