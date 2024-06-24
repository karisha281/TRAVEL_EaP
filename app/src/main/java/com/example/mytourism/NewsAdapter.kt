package com.example.mytourism

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.Model.NewsItem
import org.joda.time.format.DateTimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import android.content.Context

class NewsAdapter(
    private val newsList: List<NewsItem>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.news_title)
        val descriptionTextView: TextView = view.findViewById(R.id.news_description)
        val dateTextView: TextView = view.findViewById(R.id.news_date)

        init {
            view.setOnClickListener {
                onItemClickListener.onItemClick(newsList[adapterPosition], view.context)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.titleTextView.text = newsItem.title
        holder.descriptionTextView.text = newsItem.description
        holder.dateTextView.text = newsItem.pubDate
    }

    override fun getItemCount() = newsList.size

    interface OnItemClickListener {
        fun onItemClick(item: NewsItem, context: Context)
    }
}




