package com.example.mytourism

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("MyTourismPrefs", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "ru")
        updateLocale(savedLanguage.orEmpty())

        val languageSettings = findViewById<TextView>(R.id.language)
        languageSettings.setOnClickListener {
            openLanguageDialog()
        }

        val resetPasswordSetting = findViewById<TextView>(R.id.lock)
        resetPasswordSetting.setOnClickListener {

            startActivity(Intent(this, PasswordResetActivity::class.java))
        }

        val clickTextProgram = findViewById<TextView>(R.id.for_programm)
        clickTextProgram.setOnClickListener {

            val activityForProgram = Intent(this, ActivityForProgramm::class.java)
            startActivity(activityForProgram)
        }

        val clickTextHelp = findViewById<TextView>(R.id.help)
        clickTextHelp.setOnClickListener {

            val activityForHelp = Intent(this, HelpActivity::class.java)
            startActivity(activityForHelp)
        }

        findViewById<TextView>(R.id.notification).setOnClickListener {
            // Открыть новую активность для управления уведомлениями
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        val changeThemeButton = findViewById<TextView>(R.id.change_theme)
        changeThemeButton.setOnClickListener {
            showThemeSelectionDialog()
        }


    }

    private fun showThemeSelectionDialog() {
        val themes = arrayOf(
            getString(R.string.theme_dark),
            getString(R.string.theme_light)
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.select_theme)
            .setItems(themes) { _, which ->
                // Сохранение выбранной темы в SharedPreferences
                val selectedTheme = themes[which]
                saveThemeSelection(selectedTheme)
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }

    private fun saveThemeSelection(theme: String) {
        sharedPreferences = getSharedPreferences("MyTourismPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selected_theme", theme)
        editor.apply()

        when (theme) {
            getString(R.string.theme_dark) -> applyDarkTheme()
            getString(R.string.theme_light) -> applyLightTheme()
        }
    }

    private fun applyDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

    }

    private fun applyLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }



    private fun openLanguageDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.select_language)

        val languages = arrayOf("Русский", "English")
        builder.setItems(languages) { dialog, which ->
            handleLanguageSelection(which)
        }

        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun handleLanguageSelection(which: Int) {
        val selectedLanguage = when (which) {
            0 -> "ru"
            1 -> "en"
            else -> "ru"
        }
        saveLanguageSelection(selectedLanguage)
        updateLocale(selectedLanguage)
    }

    private fun saveLanguageSelection(language: String) {
        val editor = sharedPreferences.edit()
        editor.putString("selected_language", language)
        editor.apply()
    }

    private fun updateLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate() // Перезапускаем активность для применения изменений
    }
}
