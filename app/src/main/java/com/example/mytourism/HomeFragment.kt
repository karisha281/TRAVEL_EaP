package com.example.mytourism

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytourism.Model.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), NewsAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val newsList = mutableListOf<NewsItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_home_fragment, container, false)

        recyclerView = view.findViewById(R.id.newsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        newsAdapter = NewsAdapter(newsList, this)
        recyclerView.adapter = newsAdapter

        val noInternetView = view.findViewById<View>(R.id.textViewNoInternet)

        if (isInternetAvailable(requireContext())) {
            fetchRssNews()
            noInternetView.visibility = View.GONE
        } else {
            noInternetView.visibility = View.VISIBLE
        }
        fetchRssNews()

        return view
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onItemClick(item: NewsItem, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
        context.startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchRssNews() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm Z", Locale.ENGLISH)
                val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH)
                val doc = Jsoup.connect("https://www.votpusk.ru/news.xml").get()
                val items = doc.select("item")
                val newsItems = mutableListOf<NewsItem>()
                for (item in items) {
                    val title = item.selectFirst("title")?.text() ?: ""
                    val link = item.selectFirst("link")?.text() ?: ""
                    val description = Jsoup.parse(item.selectFirst("description")?.text() ?: "").text()
                    val dateString = item.selectFirst("pubDate")?.text() ?: ""
                    Log.d("DateString", dateString)
                    val date = try {
                        inputFormat.parse(dateString)?.let { outputFormat.format(it) } ?: ""
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ""
                    }
                    newsItems.add(NewsItem(title, link, description, date))
                }
                withContext(Dispatchers.Main) {
                    newsList.clear()
                    newsList.addAll(newsItems)
                    newsAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}