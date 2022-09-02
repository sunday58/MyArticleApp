package com.app.myarticleapp.apiSource.converter

import androidx.room.TypeConverter
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ArticleConverter {
    @TypeConverter
    fun fromString(value: String?): List<Result>? {
        val listType: Type = object : TypeToken<List<Result?>?>() {}.type
        return Gson().fromJson<List<Result>>(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<Result?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}