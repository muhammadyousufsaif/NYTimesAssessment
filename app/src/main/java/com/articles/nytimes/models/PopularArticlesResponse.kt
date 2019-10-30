package com.articles.nytimes.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PopularArticlesResponse(
    @SerializedName("status") val status : String,
    @SerializedName("copyright") val copyright : String,
    @SerializedName("num_results") val num_results : Double,
    @SerializedName("results") val results : List<Article>
): Serializable