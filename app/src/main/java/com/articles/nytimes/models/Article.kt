package com.articles.nytimes.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Article(
    @SerializedName("url") val url : String,
    @SerializedName("adx_keywords") val adx_adx_keywordswords : String,
    @SerializedName("column") val column : String,
    @SerializedName("section") val section : String,
    @SerializedName("byline") val byline : String,
    @SerializedName("type") val type : String,
    @SerializedName("title") val title : String,
    @SerializedName("abstract") val abstract : String,
    @SerializedName("published_date") val published_date : String,
    @SerializedName("source") val source : String,
    @SerializedName("id") val id : Double,
    @SerializedName("asset_id") val asset_id : Double,
    @SerializedName("views") val views : Double,
    @SerializedName("media") val media : List<Media>
): Serializable