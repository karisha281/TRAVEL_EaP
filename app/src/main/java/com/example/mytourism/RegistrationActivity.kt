package com.example.mytourism

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.mytourism.Prevalent.Prevalent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

class RegistrationActivity : AppCompatActivity() {

    private lateinit var btnCreateAc: Button
    private lateinit var loadingBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnCreateAc = findViewById(R.id.btn_new_acc)

        val passwordInput = findViewById<EditText>(R.id.input_in_passw)
        val passwordToggle = findViewById<ImageView>(R.id.password_toggle)

        // Начальное состояние - пароль скрыт
        passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        passwordToggle.setOnClickListener {
            if (passwordInput.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Показываем пароль
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_eye_on)
            } else {
                // Скрываем пароль
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_eye_off)
            }
            // Переместить курсор в конец поля ввода
            passwordInput.setSelection(passwordInput.text.length)

        }

        loadingBar = ProgressDialog(this)

        btnCreateAc.setOnClickListener {

            CreateAccount()

        }

    }
    private fun CreateAccount() {

        val inputUsername = findViewById<EditText>(R.id.input_in_username)
        val inputEmail = findViewById<EditText>(R.id.input_in_email)
        val inputPhone = findViewById<EditText>(R.id.input_in_phone)
        val inputPassword = findViewById<EditText>(R.id.input_in_passw)

        val username = inputUsername.text.toString()
        val email = inputEmail.text.toString()
        val phone = inputPhone.text.toString()
        val password = inputPassword.text.toString()
        loadingBar = ProgressDialog(this)

        when {
            TextUtils.isEmpty(username) -> {
                Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show()
            }

            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Введите почту", Toast.LENGTH_SHORT).show()
            }

            TextUtils.isEmpty(phone) -> {
                Toast.makeText(this, "Введите номер", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
            }
            else -> {
                loadingBar.setTitle("Создание аккаунта")
                loadingBar.setMessage("Пожалуйста, подождите...")
                loadingBar.setCanceledOnTouchOutside(false)
                loadingBar.show()

                ValidatePhone(username, email, phone, password)

            }
        }
    }

    private fun ValidatePhone(username: String, email: String, phone: String, password: String) {
        val rootRef = FirebaseDatabase.getInstance().reference
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.child("Users").child(phone).exists()) {

                    val userDataMap = hashMapOf(
                        "phone" to phone,
                        "name" to username,
                        "password" to password,
                        "email" to email
                    )

                    rootRef.child("Users").child(phone).updateChildren(userDataMap as Map<String, Any>)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loadingBar.dismiss()
                                Toast.makeText(
                                    this@RegistrationActivity,
                                    "Регистрация прошла успешно.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                Paper.book().write(Prevalent.UserName, username)
                                Paper.book().write(Prevalent.UserPhoneKey, phone)


                                val loginIntent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                                startActivity(loginIntent)
                            } else {
                                loadingBar.dismiss()
                                Toast.makeText(this@RegistrationActivity, "Ошибка.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    loadingBar.dismiss()
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Номер $phone уже зарегистрирован",
                        Toast.LENGTH_SHORT
                    ).show()

                    val loginIntent = Intent(this@RegistrationActivity, LoginActivity::class.java)

                    startActivity(loginIntent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
