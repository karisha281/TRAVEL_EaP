package com.example.mytourism

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.mytourism.Model.Cart
import com.example.mytourism.Model.MyTours
import com.example.mytourism.Model.Tour

class TourDetailsBeyActivity : AppCompatActivity() {

    private lateinit var cart: Cart
    private lateinit var myTours: MyTours

    private lateinit var tourNameTextView: TextView
    private lateinit var tourPriceTextView: TextView
    private lateinit var tourImageView: ImageView
    private lateinit var button_order: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_details_bey)

        cart = Cart(applicationContext)
        myTours = MyTours(applicationContext)

        tourImageView = findViewById(R.id.imageView_tour)
        tourNameTextView = findViewById(R.id.textView_tour_bey_name)
        tourPriceTextView = findViewById(R.id.textView_tour_bey_price)
        button_order = findViewById(R.id.button_bey_order)


        val tour = intent.getParcelableExtra<Tour>("tour")

        if (tour != null) {
            tourImageView.setImageResource(tour.imageResource)
            tourNameTextView.text = "Вы выбрали: ${tour.name}"
            tourPriceTextView.text = "К оплате: ${tour.price} руб."

            button_order.setOnClickListener {

                cart.removeTourFromCart(tour)

                // Добавляем тур в "Мои туры"
                myTours.addTourToMyTours(tour)

                // Создание нового intent для открытия нового окна
                val intent = Intent(this, SuccessfulPurchaseActivity::class.java)
                startActivity(intent)
            }
        }

    }
}
