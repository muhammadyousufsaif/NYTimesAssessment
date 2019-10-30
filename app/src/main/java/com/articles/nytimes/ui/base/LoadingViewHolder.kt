package com.articles.nytimes.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.articles.nytimes.R
import com.articles.nytimes.databinding.LoadingViewBinding
import com.articles.nytimes.models.LoadingModel

class LoadingViewHolder(private val parent: ViewGroup ,
                        val binding : LoadingViewBinding
                        = DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            R.layout.loading_view,parent,false)
             ): BaseViewHolder<LoadingModel>(binding.root){


    override fun bind(item: LoadingModel , position: Int) {


        binding.shimmerViewContainer.startShimmerAnimation()

    }

}