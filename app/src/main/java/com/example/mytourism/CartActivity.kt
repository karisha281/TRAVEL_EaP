    package com.example.mytourism

    import android.annotation.SuppressLint
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.mytourism.Model.Cart
    import com.example.mytourism.Model.Tour

    class CartActivity : AppCompatActivity() {
        private var cartAdapter: CartAdapter? = null
        private lateinit var cart: Cart
        private val tours: List<Tour> get() = cart.getTours()
        private val addedTours: MutableList<Tour> get() = cart.getTours().toMutableList()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_order_page)

            val recyclerView: RecyclerView = findViewById(R.id.recyclerView_cart)
            cart = Cart(this) // инициализация объекта Cart
            setupRecyclerView(recyclerView)
        }

        private fun setupRecyclerView(recyclerView: RecyclerView) {

            cartAdapter = CartAdapter(tours, addedTours) { tour ->
                removeTourFromCart(tour)
            }
            recyclerView.adapter = cartAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun removeTourFromCart(tour: Tour) {
            // Реализация удаления тура из корзины
            cart.removeTourFromCart(tour)
            cartAdapter?.notifyDataSetChanged()
        }
    }

