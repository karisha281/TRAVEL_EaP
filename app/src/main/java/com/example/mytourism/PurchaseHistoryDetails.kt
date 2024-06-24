package com.example.mytourism

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.mytourism.Model.Tour

class PurchaseHistoryDetails : AppCompatActivity() {

    private lateinit var tourNameTextView: TextView
    private lateinit var tourPriceTextView: TextView
    private lateinit var tourImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history_details)


        val tour = intent.getParcelableExtra<Tour>("tour")

        tourImageView = findViewById(R.id.imageView_tour)
        tourNameTextView = findViewById(R.id.textView_tour_bey_name)
        tourPriceTextView = findViewById(R.id.textView_tour_bey_price)

        if (tour != null) {
            tourImageView.setImageResource(tour.imageResource)


            tourNameTextView.text = "Вы выбрали: ${tour.name}"


            tourPriceTextView.text = "К оплате: ${tour.price} руб."
        }


    }
}