package com.example.mytourism.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.CartActivity
import com.example.mytourism.Model.Constants
import com.example.mytourism.Model.Tour
import com.example.mytourism.R
import com.example.mytourism.TourDetailsActivity
import java.time.LocalDate

class TourAdapter(
    private val tours: List<Tour>,
    private val onTourClicked: (Tour) -> Unit
) : RecyclerView.Adapter<TourAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tourNameTextView: TextView = itemView.findViewById(R.id.tourNameTour)
        private val tourPriceTextView: TextView = itemView.findViewById(R.id.tour_price)
        private val tourImage: ImageView = itemView.findViewById(R.id.tour_image)
        private val tourDescriptionTextView: TextView = itemView.findViewById(R.id.tour_description)
        val id: Int

        init {
            id = adapterPosition
        }

        init {
            tourImage.setOnClickListener {
                onTourClicked(tours[adapterPosition])
            }
        }

        fun bind(tour: Tour) {
            tourNameTextView.text = tour.name
            tourDescriptionTextView.text = tour.description
            tourPriceTextView.text = "${tour.price} ${Constants.CURRENCY}"
            tourImage.setImageResource(tour.imageResource)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tour_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tour = tours[position]
        holder.bind(tour)
    }

    override fun getItemCount() = tours.size



    interface OnItemClickListener {
        fun onTourDetailsClicked(tour: Tour)
    }
}