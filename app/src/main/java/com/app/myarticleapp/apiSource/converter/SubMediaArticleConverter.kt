package com.app.myarticleapp.apiSource.converter

import androidx.room.TypeConverter
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse.Result
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse.Result.Media.MediaMetadata
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SubMediaArticleConverter {
    @TypeConverter
    fun fromString(value: String?): List<MediaMetadata>? {
        val listType: Type = object : TypeToken<List<MediaMetadata?>?>() {}.type
        return Gson().fromJson<List<MediaMetadata>>(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<MediaMetadata?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}