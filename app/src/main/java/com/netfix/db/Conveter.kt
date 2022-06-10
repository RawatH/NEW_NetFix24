package com.netfix.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.netfix.models.network.dataModel.UserPlanVO
import java.util.*

class Conveter {
    @TypeConverter
    fun fromCountryLangList(value: List<UserPlanVO>): String {
        val gson = Gson()
        val type = object : TypeToken<List<UserPlanVO>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCountryLangList(value: String): List<UserPlanVO> {
        val gson = Gson()
        val type = object : TypeToken<List<UserPlanVO>>() {}.type
        return gson.fromJson(value, type)
    }
}