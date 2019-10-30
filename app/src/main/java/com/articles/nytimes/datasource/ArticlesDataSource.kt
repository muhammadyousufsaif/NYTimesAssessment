package com.articles.nytimes.datasource

import com.articles.nytimes.models.LoadingModel

class ArticlesDataSource {

    fun getItemsPage(): List<Any> {
        val items = mutableListOf<Any>()
        items.add(LoadingModel())
        return items
    }
}