package com.app.myarticleapp.apiSource.responseEntity


import androidx.room.Entity
import androidx.room.TypeConverters
import com.app.myarticleapp.apiSource.converter.ArticleConverter
import com.app.myarticleapp.apiSource.converter.MediaArticleConverter
import com.app.myarticleapp.apiSource.converter.SubMediaArticleConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "article_table")
data class ArticleResponse(
    @SerializedName("copyright") val copyright: String = "",
    @SerializedName("num_results") val numResults: Int = 0,
    @SerializedName("results")
    @TypeConverters(ArticleConverter::class) val results: List<Result> = listOf(),
    @SerializedName("status") val status: String = ""
)

data class Result(
    @SerializedName("abstract") val `abstract`: String = "",
    @SerializedName("adx_keywords") val adxKeywords: String = "",
    @SerializedName("asset_id") val assetId: Long = 0,
    @SerializedName("byline") val byline: String = "",
    @SerializedName("id") val id: Long = 0,
    @SerializedName("media")
    @TypeConverters(MediaArticleConverter::class) val media: List<Media> = listOf(),
    @SerializedName("nytdsection") val nytSection: String = "",
    @SerializedName("published_date") val publishedDate: String = "",
    @SerializedName("section") val section: String = "",
    @SerializedName("source") val source: String = "",
    @SerializedName("subsection") val subsection: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("updated") val updated: String = "",
    @SerializedName("uri") val uri: String = "",
    @SerializedName("url") val url: String = ""
)

data class Media(
    @SerializedName("approved_for_syndication") val approvedForSyndication: Int = 0,
    @SerializedName("caption") val caption: String = "",
    @SerializedName("copyright") val copyright: String = "",
    @SerializedName("media-metadata")
    @TypeConverters(SubMediaArticleConverter::class) val mediaMetadata: List<MediaMetadata> = listOf(),
    @SerializedName("subtype") val subtype: String = "",
    @SerializedName("type") val type: String = ""
)

data class MediaMetadata(
    @SerializedName("format") val format: String = "",
    @SerializedName("height") val height: Int = 0,
    @SerializedName("url") val url: String = "",
    @SerializedName("width") val width: Int = 0
)

