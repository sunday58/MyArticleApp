package com.app.myarticleapp.apiSource.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import com.app.myarticleapp.apiSource.responseEntity.Result as ArticleResult

class ArticleConverter {
    @TypeConverter
    fun fromString(value: String?): List<ArticleResult>? {
        val listType: Type = object : TypeToken<List<ArticleResult?>?>() {}.type
        return Gson().fromJson<List<ArticleResult>>(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<ArticleResult?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}