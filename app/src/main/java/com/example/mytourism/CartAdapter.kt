    package com.example.mytourism

    import android.app.AlertDialog
    import android.content.Context
    import android.content.Intent
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageButton
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.mytourism.Model.Tour

    class CartAdapter(
        private val tours: List<Tour>,
        private val addedTours: MutableList<Tour>,
        private val onTourRemoved: (Tour) -> Unit
    ) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val btnRemove: ImageButton = itemView.findViewById(R.id.button_remove)
            val nameTextView: TextView = itemView.findViewById(R.id.textView_tour_name)
            val priceTextView: TextView = itemView.findViewById(R.id.textView_tour_price)
            val imageView: ImageView = itemView.findViewById(R.id.imageView_tour)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.order_item_list, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val tour = addedTours[position]
            holder.nameTextView.text = tour.name
            holder.priceTextView.text = tour.price.toString()
            holder.imageView.setImageResource(tour.imageResource)

            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, TourDetailsBeyActivity::class.java)
                intent.putExtra("tour", tour)
                it.context.startActivity(intent)
            }

            holder.btnRemove.setOnClickListener {
                val removedPosition = addedTours.indexOf(tour)
                showDeleteConfirmationDialog(it.context, tour, removedPosition)
            }
        }

        private fun showDeleteConfirmationDialog(context: Context, tour: Tour, removedPosition: Int) {
            AlertDialog.Builder(context)
                .setTitle("Удалить")
                .setMessage("Вы действительно хотите удалить тур?")
                .setPositiveButton("Да") { _, _ ->
                    addedTours.remove(tour)
                    notifyItemRemoved(removedPosition)
                    onTourRemoved(tour)
                }
                .setNegativeButton("Нет") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        override fun getItemCount() = addedTours.size
    }