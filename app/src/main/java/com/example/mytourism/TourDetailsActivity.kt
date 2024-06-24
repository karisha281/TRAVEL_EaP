package com.example.mytourism

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.Model.Cart
import com.example.mytourism.Model.Constants
import com.example.mytourism.Model.Tour

class TourDetailsActivity : AppCompatActivity() {
    private lateinit var tourImageView: ImageView
    private lateinit var tourNameTextView: TextView
    private lateinit var tourDaysTextView: TextView
    private lateinit var tourHotelTextView: TextView
    private lateinit var tourPriceTextView: TextView
    private lateinit var emptyCartText: TextView
    private lateinit var recyclerView: RecyclerView
    private var cartTours: MutableList<Tour> = mutableListOf()

    private val cart: Cart by lazy {
        val context = applicationContext ?: throw IllegalStateException("Application context is null")
        Cart(context)
    }

    private val cartActivity = CartActivity()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_details)

        // Инициализация элементов
        tourImageView = findViewById(R.id.tour_details_image)
        tourNameTextView = findViewById(R.id.tourNameTour_details)
        tourDaysTextView = findViewById(R.id.tour_details_days)
        tourHotelTextView = findViewById(R.id.tour_details_hotel)
        tourPriceTextView = findViewById(R.id.tour_details_price)
        emptyCartText = findViewById(R.id.empty_cart_text)


        val tour = intent.getParcelableExtra<Tour>("tour")
        tour?.let {
            populateTourDetails(it)
        }


        tourImageView.setOnClickListener {
            toggleTourImageSize()
        }

        val addToCartButton: Button = findViewById(R.id.button_in_cart)
        addToCartButton.setOnClickListener {
            cart.addTourToCart(tour!!)
            Toast.makeText(this, "Тур был добавлен в корзину", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addTourToCart(tour: Tour) {
        cart.addTourToCart(tour)
        // Обновите UI или отправьте уведомление пользователю
    }


    private fun toggleTourImageSize() {
        if (tourImageView.scaleX == 1f) {
            tourImageView.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(500)
                .start()
        } else {
            tourImageView.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .start()
        }
    }

    private fun populateTourDetails(tour: Tour) {
        tourImageView.setImageResource(tour.imageResource)
        tourNameTextView.text = tour.name
        tourDaysTextView.text = "Количество дней: ${tour.days} дня"
        tourHotelTextView.text = "Место проживания: ${tour.hotel}"
        tourPriceTextView.text = "Цена тура: ${tour.price} ${Constants.CURRENCY}"
    }
}
