package com.articles.nytimes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.articles.nytimes.R
import com.articles.nytimes.databinding.ItemArticleViewBinding
import com.articles.nytimes.enums.RecyclerEnum
import com.articles.nytimes.models.Article
import com.articles.nytimes.models.EmptyModel
import com.articles.nytimes.models.LoadingModel
import com.articles.nytimes.ui.base.BaseViewHolder
import com.articles.nytimes.ui.base.EmptyViewHolder
import com.articles.nytimes.ui.base.LoadingViewHolder
import com.articles.nytimes.ui.iface.OnItemListener

class ArticlesAdapter: RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var items: List<Any> = emptyList()

    private var listener : OnItemListener? = null
    var selectedItemView : ItemArticleViewBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        return  when(viewType){
            RecyclerEnum.LOADER_VIEW.ordinal->  LoadingViewHolder(parent)
            RecyclerEnum.EMPTY_VIEW.ordinal->  EmptyViewHolder(parent,listener)
            RecyclerEnum.DATA_VIEW.ordinal-> ArticleViewHolder(parent)
            else ->  LoadingViewHolder(parent)
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {

        var viewModel = items[position]

        if (holder is LoadingViewHolder && items.size > position) {

            holder.bind(viewModel as LoadingModel,position)

        }else  if (holder is EmptyViewHolder && items.size > position) {

            holder.bind(viewModel as EmptyModel,position)

        }else if (holder is ArticleViewHolder && items.size > position){

            holder.bind(viewModel as Article, position)

        }
    }

    /**
     *
     */
    fun update(items: List<Any>) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun RecyclerView.bindItems(items: List<Any>) {
            val adapter = adapter as ArticlesAdapter
            adapter.update(items)
        }
    }

    /**
     *
     */
    override fun getItemViewType(position: Int): Int {

        var viewModel = items[position]

        if(viewModel is LoadingModel){
            return  RecyclerEnum.LOADER_VIEW.ordinal
        }else if(viewModel is EmptyModel){
            return  RecyclerEnum.EMPTY_VIEW.ordinal
        }else{
            return  RecyclerEnum.DATA_VIEW.ordinal
        }

    }

    /**
     *
     *
     */
    inner class ArticleViewHolder(private val parent: ViewGroup,
                                  private val binding : ItemArticleViewBinding
                                    = DataBindingUtil.inflate(
                                        LayoutInflater.from(parent.context),
                                        R.layout.item_article_view, parent,false))
        : BaseViewHolder<Article>(binding.root){

        override fun bind(item: Article , position: Int) {
            binding.title = item.title
            binding.byline = item.byline
            if (item.media.count() > 0 && item.media[0].mediaMetadata.count() > 0) {
                val metaDataArray = item.media[0].mediaMetadata
                val metaData = metaDataArray.find { it.format == "Standard Thumbnail" }
                if (metaData != null) {
                    binding.imgUrl = metaData.url
                }
            }
            binding.publishedDate = item.published_date
            binding.itemRootView.setOnClickListener {
                selectedItemView = binding
                listener?.onItemClicked(item)
            }
        }
    }

    fun addListener(listener: OnItemListener){
        this.listener  = listener
    }

}
