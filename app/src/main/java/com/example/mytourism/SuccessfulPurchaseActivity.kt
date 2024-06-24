package com.example.mytourism

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SuccessfulPurchaseActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_purchase)

        val messageTextView = findViewById<TextView>(R.id.textView_success_message)
        val messageTextView2 = findViewById<TextView>(R.id.textView_success_message2)

        // Установка текста в TextView
        messageTextView.text = "Вы успешно приобрели тур!"
        messageTextView2.text = "Желаем хорошего отдыха!"
    }
}