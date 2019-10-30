package com.articles.nytimes.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.articles.nytimes.R
import com.articles.nytimes.databinding.ItmeEmptyListBinding
import com.articles.nytimes.models.EmptyModel
import com.articles.nytimes.ui.iface.OnItemListener

class EmptyViewHolder(private val parent: ViewGroup,
                      private val listener: OnItemListener?,
                      val binding : ItmeEmptyListBinding =
                          DataBindingUtil.inflate(
                          LayoutInflater.from(parent.context),
                          R.layout.itme_empty_list,parent,false)): BaseViewHolder<EmptyModel>(binding.root){




    override fun bind(item: EmptyModel , position: Int) {

        binding.retryBtn.setOnClickListener{
            listener?.onRetry()
        }

    }

}