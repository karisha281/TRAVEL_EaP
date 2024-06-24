package com.example.mytourism

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.Model.Cart
import com.example.mytourism.Model.MyTours
import com.example.mytourism.Model.Tour

class PurchaseHistory : AppCompatActivity() {
    private var purchaseHistoryAdapter: PurchaseHistoryAdapter? = null
    private lateinit var myTours: MyTours
    private val tours: List<Tour> get() = myTours.getTours()
    private val addedTours: MutableList<Tour> get() = myTours.getTours().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView_cart)
        myTours = MyTours(applicationContext)
        setupRecyclerView(recyclerView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        purchaseHistoryAdapter = PurchaseHistoryAdapter(tours, addedTours) { tour ->
            removeTourFromMyTours(tour)
        }
        recyclerView.adapter = purchaseHistoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeTourFromMyTours(tour: Tour) {
        myTours.removeTourFromMyTours(tour)
        purchaseHistoryAdapter?.notifyDataSetChanged()
    }
}