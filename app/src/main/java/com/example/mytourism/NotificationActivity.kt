package com.example.mytourism

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.util.Calendar

class NotificationActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var notificationSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // Найти и настроить переключатель уведомлений
        notificationSwitch = findViewById(R.id.notification_switch)
        notificationSwitch.isChecked = isNotificationsEnabled()
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            setNotificationsEnabled(isChecked)
        }
    }

    private fun isNotificationsEnabled(): Boolean {
        // Реализуйте логику проверки состояния уведомлений
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("notifications_enabled", true)
    }

    private fun setNotificationsEnabled(isEnabled: Boolean) {
        // Реализуйте логику включения/выключения уведомлений
        if (isEnabled) {
            scheduleReminders()
        } else {
            cancelReminders()
        }

        // Сохраните состояние уведомлений в SharedPreferences
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("notifications_enabled", isEnabled).apply()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleReminders() {
        // Получить экземпляр AlarmManager
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Создать PendingIntent для ReminderReceiver
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Установить будильник, который будет срабатывать каждую минуту
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 60000, // Минута в миллисекундах
            pendingIntent
        )

        // Создать канал уведомлений для Android 8.0 и выше
        createNotificationChannel()
    }

    private fun cancelReminders() {
        // Реализуйте логику для отмены всех запланированных напоминаний
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    private fun createNotificationChannel() {
        // Создайте канал уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "travel_reminders",
                "Напоминания о путешествиях",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
