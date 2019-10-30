package com.articles.nytimes.network

import com.articles.nytimes.models.PopularArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("mostpopular/v2/mostviewed/{section}/{period}.json")
    abstract fun getLatestArticles(@Path("section") section: String,
                                   @Path("period") period: String,
                                   @Query("api-key") apiKey: String): Call<PopularArticlesResponse>

}