    package com.example.mytourism

    import android.annotation.SuppressLint
    import android.app.ProgressDialog
    import android.content.Context
    import android.content.Intent
    import android.net.ConnectivityManager
    import android.os.Bundle
    import android.text.InputType
    import android.widget.Button
    import android.widget.CheckBox
    import android.widget.EditText
    import android.widget.ImageView
    import android.widget.TextView
    import android.widget.Toast
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import com.example.mytourism.Model.Users
    import com.example.mytourism.Prevalent.Prevalent
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.DatabaseReference
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener
    import io.paperdb.Paper

    class LoginActivity : AppCompatActivity() {

        private lateinit var loadingBar: ProgressDialog
        private var parentDbName = "Users"
        private lateinit var checkBoxRememberMe: CheckBox

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)

            val btnLogin = findViewById<Button>(R.id.button_login)
            checkBoxRememberMe = findViewById(R.id.checkBoxRemeber)
            val passwordInput = findViewById<EditText>(R.id.password)
            val passwordToggle = findViewById<ImageView>(R.id.password_toggle)

            val newPasswordText = findViewById<TextView>(R.id.new_password)


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

            newPasswordText.setOnClickListener {
                // Открываем класс PasswordReset
                val intent = Intent(this@LoginActivity, PasswordResetActivity::class.java)
                startActivity(intent)
            }
            Paper.init(this)

            btnLogin.setOnClickListener { loginUser() }
        }

        private fun loginUser() {
            loadingBar = ProgressDialog(this)

            val inputPhone = findViewById<EditText>(R.id.login)
            val inputPassword = findViewById<EditText>(R.id.password)

            val phone = inputPhone.text.toString()
            val password = inputPassword.text.toString()

            if (phone.isEmpty()) {
                Toast.makeText(this, "Введите номер", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
            } else {
                loadingBar.setTitle("Вход в приложение")
                loadingBar.setMessage("Пожалуйста, подождите...")
                loadingBar.setCanceledOnTouchOutside(false)
                loadingBar.show()

                ValidateUser(phone, password)
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
            val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

            rootRef.child(parentDbName).child(phone)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val usersData = dataSnapshot.getValue(Users::class.java)

                            if (usersData != null && usersData.phone == phone && usersData.password == password ) {
                                saveUserData(usersData.getName(), usersData.getPhone())

                                if (checkBoxRememberMe.isChecked) {
                                    Paper.book().write(Prevalent.UserPhoneKey, usersData.phone)
                                    Paper.book().write(Prevalent.UserPasswordKey, usersData.password) // Сохраняем имя пользователя
                                }

                                loadingBar.dismiss()
                                Toast.makeText(this@LoginActivity, "Успешный вход!", Toast.LENGTH_SHORT).show()

                                val userName = usersData.getName()
                                val phoneName = usersData.getPhone()
                                Paper.book().write(Prevalent.UserName, userName ?: "")
                                Paper.book().write(Prevalent.UserPhoneKey, phoneName)

                                val homeIntent = Intent(this@LoginActivity, MenuActivity::class.java)
                                startActivity(homeIntent)

                            } else {
                                loadingBar.dismiss()
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Неверный номер телефона или пароль",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            loadingBar.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                "Учетная запись с таким номером телефона не найдена",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        loadingBar.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            "Ошибка при подключении к базе данных",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                showNoInternetDialog()
            }
        }


        private fun saveUserData(userName: String, phoneNumber: String) {
            val sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("user_name", userName)
            editor.putString("phone_number", phoneNumber)
            editor.apply()
        }
    }