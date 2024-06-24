package com.example.mytourism

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mytourism.Model.Users
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import java.net.URLEncoder
import java.util.UUID


class PasswordResetActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        emailEditText = findViewById(R.id.emailEditText)
        resetPasswordButton = findViewById(R.id.resetPasswordButton)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString()
            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {
        databaseReference.child("Users").orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(Users::class.java)
                            user?.let {
                                showResetPasswordDialog(it.phone, it.password)
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@PasswordResetActivity,
                            "Пользователь с таким email не найден",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@PasswordResetActivity,
                        "Ошибка базы данных: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showResetPasswordDialog(phone: String, currentPassword: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Сменить пароль")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val newPassword = input.text.toString()

            // Обновить пароль в базе данных
            val updatedData = mapOf("password" to newPassword)
            databaseReference.child("Users").child(phone).updateChildren(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(
                        this@PasswordResetActivity,
                        "Пароль успешно изменен",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this@PasswordResetActivity,
                        "Не удалось изменить пароль: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        builder.setNegativeButton("Отмена") { _, _ -> }
        builder.show()
    }
}
