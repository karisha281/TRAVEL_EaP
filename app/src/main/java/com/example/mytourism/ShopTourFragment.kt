package com.example.mytourism

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.Model.Country

class ShopTourFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_shop_tour, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tours = listOf(
            Country(
                name = "Россия",
                imageResource = R.drawable.russia_image
            ),
            Country(
                name = "Турция",
                imageResource = R.drawable.turkey_image
            ),
            Country(
                name = "Египет",
                imageResource = R.drawable.egypt_image
            ),
        )
        adapter = CountryAdapter(tours) { country ->
            onCountryClicked(country)
        }
        recyclerView.adapter = adapter

        return view

    }

    private fun onCountryClicked(country: Country) {
        // Обработка нажатия на страну
        // Здесь вы можете открыть новую активность со списком туров для выбранной страны
        val intent = Intent(requireContext(), ShopTourActivity2::class.java)
        intent.putExtra("country_name", country.name)
        startActivity(intent)
    }

    }