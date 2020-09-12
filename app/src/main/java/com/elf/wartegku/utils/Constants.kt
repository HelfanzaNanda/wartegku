package com.elf.wartegku.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class Constants {
    companion object{

        val ENDPOINT = "https://wartegku.herokuapp.com/"
        fun getToken(c : Context) : String {
            val s = c.getSharedPreferences("USER", MODE_PRIVATE)
            val token = s?.getString("TOKEN", "UNDEFINED")
            return token!!
        }

        fun setToken(context: Context, token : String){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", token)
                apply()
            }
        }

        fun setFirstTime(context: Context, value : Boolean){
            val pref = context.getSharedPreferences("UTILS", MODE_PRIVATE)
            pref.edit().apply{
                putBoolean("FIRST_TIME", value)
                apply()
            }
        }

        fun isFirstTime(context: Context) : Boolean {
            val pref = context.getSharedPreferences("UTILS", MODE_PRIVATE)
            val bool = pref.getBoolean("FIRST_TIME", true)
            return bool
        }

        fun clearToken(context: Context){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidEmail(email : String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(pass : String) = pass.length >= 8
        fun isAlpha(name : String) = Pattern.matches("[a-zA-Z]+", name)

        fun setToIDR(num : Int) : String {
            val localeID = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(num)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun changeFormatDate(date : String) : String{
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            return current.format(formatter)
        }

        fun getSafeSubstring(s : String, maxLenght : Int) : String {
            if(!TextUtils.isEmpty(s)){
                if (s.length >= maxLenght){
                    return  s.substring(0, maxLenght)
                }
            }
            return s
        }
    }
}