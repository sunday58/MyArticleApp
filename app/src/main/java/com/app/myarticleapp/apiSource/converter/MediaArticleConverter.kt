package com.app.myarticleapp.apiSource.converter

import androidx.room.TypeConverter
import com.app.myarticleapp.apiSource.responseEntity.Media
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MediaArticleConverter {
    @TypeConverter
    fun fromString(value: String?): List<Media>? {
        val listType: Type = object : TypeToken<List<Media?>?>() {}.type
        return Gson().fromJson<List<Media>>(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<Media?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}