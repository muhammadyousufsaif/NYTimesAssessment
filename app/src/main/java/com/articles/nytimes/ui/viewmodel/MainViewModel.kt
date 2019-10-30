package com.articles.nytimes.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.articles.nytimes.BuildConfig
import com.articles.nytimes.datasource.ArticlesDataSource
import com.articles.nytimes.models.PopularArticlesResponse
import com.articles.nytimes.models.Article
import com.articles.nytimes.models.EmptyModel
import com.articles.nytimes.network.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val popularLiveData = MutableLiveData<List<Any>>()
    var allData = ArrayList<Article>()


    val articleList: LiveData<List<Any>> =
        MutableLiveData<List<Any>>().apply {
            value = ArticlesDataSource().getItemsPage()
        }

    fun getLatestArticles(context: Context, isPullToRefreshRequest: Boolean, section: String, period: String) {

        var fetchDataList = ArrayList<Any>()
        var call = RestClient().getClient(context, isPullToRefreshRequest)
            .getLatestArticles(section, period, BuildConfig.API_KEY)

        call.enqueue(object : Callback<PopularArticlesResponse> {
            override fun onResponse(call: Call<PopularArticlesResponse>, response: Response<PopularArticlesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val bodyResponse = response.body()
                    if(bodyResponse?.results?.isEmpty()!! && fetchDataList.count() == 0) {
                        fetchDataList.add(EmptyModel())
                    } else {
                        fetchDataList.addAll(bodyResponse.results)
                        allData.addAll(bodyResponse.results)
                    }
                } else {
                    fetchDataList.add(EmptyModel())
                }
                popularLiveData.postValue(fetchDataList)
            }

            override fun onFailure(call: Call<PopularArticlesResponse>, t: Throwable) {
                fetchDataList.add(EmptyModel())
                popularLiveData.postValue(fetchDataList)

            }
        })
    }

    fun filterNews(query: String) {
        if (query.isNotEmpty()) {
            var filteredList =
                allData.filter { item -> (item.byline.contains(query, true) ||
                        item.title.contains(query, true) ||
                        item.published_date.contains(query, true)) }
            popularLiveData.postValue(filteredList)
        } else {
            popularLiveData.postValue(allData)
        }

    }

}
