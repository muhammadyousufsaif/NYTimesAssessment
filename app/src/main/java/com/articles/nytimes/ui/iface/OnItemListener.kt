package com.articles.nytimes.ui.iface

import com.articles.nytimes.models.Article

interface OnItemListener {
    fun onRetry()
    fun onItemClicked(article: Article)
}