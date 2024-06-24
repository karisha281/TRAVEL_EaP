package com.example.mytourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.Model.Country

class CountryAdapter(private val tours: List<Country>,
                     private val onCountryClicked: (Country) -> Unit)
    : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tourNameCountry)
        val imageView: ImageView = itemView.findViewById(R.id.country_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tour = tours[position]
        holder.imageView.setImageResource(tour.imageResource)
        holder.nameTextView.text = tour.name


        holder.itemView.setOnClickListener {
            onCountryClicked(tour)
        }
    }

    override fun getItemCount(): Int {
        return tours.size
    }
}