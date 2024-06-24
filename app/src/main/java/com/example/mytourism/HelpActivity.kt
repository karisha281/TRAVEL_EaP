package com.example.mytourism

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class HelpActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var editText: EditText
    private lateinit var problemDescriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        spinner = findViewById(R.id.problem_spinner)
        editText = findViewById(R.id.problem_description)
        problemDescriptionTextView = findViewById(R.id.problem_description_text)

        // Заполнение списка проблем
        val problemsList = listOf(
            getString(R.string.problem_auth),
            getString(R.string.problem_booking),
            getString(R.string.problem_payment),
            getString(R.string.problem_other)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, problemsList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Обработка выбора проблемы из списка
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedProblem = problemsList[position]
                updateProblemDescription(selectedProblem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Обработка нажатия на кнопку "Отправить"
        findViewById<View>(R.id.send_button).setOnClickListener {
            val problemDescription = editText.text.toString().trim()
            if (problemDescription.isNotEmpty()) {
                // Вместо отправки в Telegram Bot, можно сделать что-то другое, например,
                // отправить описание проблемы на сервер или сохранить локально
                // ...
                showProblemSentToast()
            } else {
                // Показать сообщение, что необходимо ввести описание проблемы
            }
        }
    }

    private fun updateProblemDescription(selectedProblem: String) {
        when (selectedProblem) {
            getString(R.string.problem_auth) -> {
                problemDescriptionTextView.text = getString(R.string.problem_auth_desc)
            }
            getString(R.string.problem_booking) -> {
                problemDescriptionTextView.text = getString(R.string.problem_booking_desc)
            }
            getString(R.string.problem_payment) -> {
                problemDescriptionTextView.text = getString(R.string.problem_payment_desc)
            }
            getString(R.string.problem_other) -> {
                problemDescriptionTextView.text = getString(R.string.problem_other_desc)
            }
        }
    }

    private fun showProblemSentToast() {
        Toast.makeText(this, getString(R.string.problem_sent_toast), Toast.LENGTH_SHORT).show()
    }
}