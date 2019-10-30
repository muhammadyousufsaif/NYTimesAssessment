package com.articles.nytimes.ui.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.articles.nytimes.R
import com.articles.nytimes.models.Article

class ArticleDetailsActivity : AppCompatActivity() {

    private var article: Article? = null

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_article_details)

        article = intent.getSerializableExtra("EXTRA_ARTICLE") as? Article

        webView = findViewById(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        webView.loadUrl(article?.url)
    }

}