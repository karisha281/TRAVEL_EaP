package com.example.mytourism

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mytourism.Model.Users
import com.example.mytourism.Prevalent.Prevalent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistration = findViewById<Button>(R.id.registration_in_app)
        val btnLogin = findViewById<Button>(R.id.login_in_app)

        Paper.init(this)

        btnRegistration.setOnClickListener {
            val activityForRegistration = Intent(this, RegistrationActivity::class.java)
            startActivity(activityForRegistration)
        }

        btnLogin.setOnClickListener {
            val activityForLogin = Intent(this, LoginActivity::class.java)
            startActivity(activityForLogin)
        }

        val userPhoneKey = Paper.book().read<String>(Prevalent.UserPhoneKey)
        val userPasswordKey = Paper.book().read<String>(Prevalent.UserPasswordKey)

        if (userPhoneKey != null && userPasswordKey != null) {
            if (!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey)) {
                ValidateUser(userPhoneKey, userPasswordKey)
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Нет интернет-соединения")
        builder.setMessage("Пожалуйста, проверьте ваше интернет-соединение и попробуйте снова.")
        builder.setPositiveButton("ОК") { _, _ ->
            finish() // Закрытие приложения при нажатии на "ОК"
        }
        builder.show()
    }

    private fun ValidateUser(phone: String, password: String) {

        if (checkInternetConnection()) {

        val loadingBar = ProgressDialog(this)
        loadingBar.setTitle("Вход в приложение")
        loadingBar.setMessage("Пожалуйста, подождите...")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()) {
                    val usersData =
                        dataSnapshot.child("Users").child(phone).getValue(Users::class.java)

                    if (usersData != null && usersData.phone == phone) {
                        if (usersData.password == password) {
                            loadingBar.dismiss()
                            Toast.makeText(
                                this@MainActivity,
                                "Успешный вход!",
                                Toast.LENGTH_SHORT
                            ).show()

                            Paper.book().write(Prevalent.UserName, usersData.getName())
                            Paper.book().write(Prevalent.UserPhoneKey, phone)

                            val homeIntent = Intent(this@MainActivity, MenuActivity::class.java)
                            startActivity(homeIntent)
                        } else {
                            loadingBar.dismiss()
                        }
                    } else {
                        loadingBar.dismiss()
                        Toast.makeText(
                            this@MainActivity,
                            "Аккаунт с номером $phone не существует",
                            Toast.LENGTH_SHORT
                        ).show()
                        val registerIntent = Intent(this@MainActivity, RegistrationActivity::class.java)
                        startActivity(registerIntent)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки
            }
        })
    } else {
            showNoInternetDialog()
        }
    }
}

