package com.dicoding.submissiondua.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.Message
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.dicoding.submissiondua.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "Github Reminder"
        private const val TIME_FORMAT = "HH:mm"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_TYPE = "extra_type"
        private const val REPEATING_ID = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        sendNotifikasi(context)
    }

    private fun sendNotifikasi(context: Context) {
        val intent = context?.packageManager.getLaunchIntentForPackage("com.dicoding.submissiondua")
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(context.resources.getString(R.string.app_name))
            .setContentText(context.resources.getString(R.string.notification))
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notificaiton = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notificaiton)
    }

    fun setUlangAlarm(context: Context, type: String, time: String, message: String) {
        if (isTanggalInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val mIntent = Intent(context, AlarmReceiver::class.java)
        mIntent.putExtra(EXTRA_MESSAGE, message)
        mIntent.putExtra(EXTRA_TYPE, type)
        val timeArray = time.split(":".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()
        val kalender = Calendar.getInstance()
        kalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        kalender.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        kalender.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, REPEATING_ID, mIntent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, kalender.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, R.string.repeating_succes, Toast.LENGTH_SHORT).show()
    }
    fun cancelAlarm(context: Context){
        val managerAlarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = REPEATING_ID
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        managerAlarm.cancel(pendingIntent)
        Toast.makeText(context, R.string.repeating_failed, Toast.LENGTH_SHORT).show()
    }

    private fun isTanggalInvalid(time: String, timeFormat: String): Boolean {
        return try {
            val dateformat = SimpleDateFormat(timeFormat, Locale.getDefault())
            dateformat.isLenient = false
            dateformat.parse(time)
            false
        }catch (e:ParseException){
            true
        }
    }
}