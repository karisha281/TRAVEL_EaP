package com.example.mytourism

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mytourism.Prevalent.Prevalent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

class UserProfileSettings : AppCompatActivity() {

    private lateinit var setting_phone: EditText
    private lateinit var setting_username: EditText
    private lateinit var setting_adress: EditText
    private lateinit var setting_surname: EditText
    private lateinit var setting_name: EditText
    private lateinit var setting_patronymic: EditText
    private lateinit var setting_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_settings)

        setting_phone = findViewById(R.id.settings_phone)
        setting_username = findViewById(R.id.settings_username_profile)

        setting_adress = findViewById(R.id.settings_address)
        setting_surname = findViewById(R.id.settings_surname)
        setting_name = findViewById(R.id.settings_name)
        setting_patronymic = findViewById(R.id.settings_patronymic)
        setting_button = findViewById(R.id.btn_save_info)



        loadUserProfile()
        loadUserData()



        setting_button.setOnClickListener {
            saveUserProfile()
            // Переход в UserActivity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

    }
    private fun loadUserData() {
        val sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", "")
        val phoneNumber = sharedPreferences.getString("phone_number", "")

        setting_username.setText(userName)
        setting_phone.setText(phoneNumber)
    }

    private fun loadUserProfile() {
        val sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE)

        val phoneNumber = sharedPreferences.getString("phone_number", "")
        val username = sharedPreferences.getString("username", "")
        val address = sharedPreferences.getString("address", "")
        val surname = sharedPreferences.getString("surname", "")
        val name = sharedPreferences.getString("name", "")
        val patronymic = sharedPreferences.getString("patronymic", "")

        setting_phone.setText(phoneNumber)
        setting_username.setText(username)
        setting_adress.setText(address)
        setting_surname.setText(surname)
        setting_name.setText(name)
        setting_patronymic.setText(patronymic)
    }

    private fun saveUserProfile() {

        val phoneNumber = setting_phone.text.toString()
        val username = setting_username.text.toString()
        val address = setting_adress.text.toString()
        val surname = setting_surname.text.toString()
        val name = setting_name.text.toString()
        val patronymic = setting_patronymic.text.toString()

        // Проверьте, что все поля заполнены
            // Получите объект SharedPreferences
            val sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Сохраните данные в SharedPreferences
            editor.putString("phone_number", phoneNumber)
            editor.putString("username", username)
            editor.putString("address", address)
            editor.putString("surname", surname)
            editor.putString("name", name)
            editor.putString("patronymic", patronymic)
            editor.apply()

            // Выведите сообщение об успешном сохранении
            Toast.makeText(this, "Профиль успешно сохранен", Toast.LENGTH_SHORT).show()
        }
    }
