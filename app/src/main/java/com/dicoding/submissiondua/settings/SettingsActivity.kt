package com.dicoding.submissiondua.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.submissiondua.R
import com.dicoding.submissiondua.databinding.ActivitySettingsBinding
import com.dicoding.submissiondua.model.Reminder
import com.dicoding.submissiondua.preference.ReminderPreference
import com.dicoding.submissiondua.receiver.AlarmReceiver

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var reminder : Reminder
    private lateinit var alarmReceiver: AlarmReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Settings"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val reminderPreference = ReminderPreference(this)
        if(reminderPreference.getReminder().isReminded){
            binding.switchone.isChecked = true
        }else {
            binding.switchone.isChecked = false
        }
        alarmReceiver = AlarmReceiver()
        binding.switchone.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                saveReminder(true)
                alarmReceiver.setUlangAlarm(this, "Repeating Alarm","09:00","GithubReminder")
            }else{
                saveReminder(false)
                alarmReceiver.cancelAlarm(this)
            }
        }
    }

    private fun saveReminder(state: Boolean) {
        val reminderPreference = ReminderPreference(this)
        reminder = Reminder()
        reminder.isReminded = state
        reminderPreference.setReminder(reminder)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}