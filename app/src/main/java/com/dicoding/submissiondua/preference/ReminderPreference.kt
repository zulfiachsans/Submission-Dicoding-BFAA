package com.dicoding.submissiondua.preference

import android.content.Context
import com.dicoding.submissiondua.model.Reminder

class ReminderPreference (context: Context){
    companion object {
        const val PREF_REM = "pref_rem"
        private const val PENGINGAT = "isReminder"
    }
    private val preference = context.getSharedPreferences(PREF_REM, Context.MODE_PRIVATE)
    fun setReminder(value: Reminder){
        val editor = preference.edit()
        editor.putBoolean(PENGINGAT, value.isReminded)
        editor.apply()
    }
    fun getReminder(): Reminder{
        val model = Reminder()
        model.isReminded = preference.getBoolean(PENGINGAT, false)
        return model
    }
}