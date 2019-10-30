package com.articles.nytimes.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.articles.nytimes.R
import com.articles.nytimes.databinding.MainFragmentBinding
import com.articles.nytimes.datasource.ArticlesDataSource
import com.articles.nytimes.models.Article
import com.articles.nytimes.ui.activities.ArticleDetailsActivity
import com.articles.nytimes.ui.adapters.ArticlesAdapter
import com.articles.nytimes.ui.iface.OnItemListener
import com.articles.nytimes.ui.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private var adapter = ArticlesAdapter()

    private var section = "all-sections"

    private var period = "1"

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
     ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner= this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerView.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL ,false)

        binding?.pullToRefresh.setOnRefreshListener {
            getLiveData(true)
        }

        binding.recyclerView.adapter = adapter
        adapter.addListener(object : OnItemListener {

            override fun onRetry() {
                adapter.update(ArticlesDataSource().getItemsPage())
                getLiveData(false)
            }

            override fun onItemClicked(article: Article) {
                val intent = Intent (getActivity(), ArticleDetailsActivity::class.java)
                intent.putExtra("EXTRA_ARTICLE", article)
                getActivity()?.startActivity(intent)
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel?.popularLiveData?.observe(viewLifecycleOwner, Observer {
            adapter.update(it)
            binding?.pullToRefresh.isRefreshing = false
        })
        getLiveData(false)
    }

    private fun getLiveData(isPullToRefreshRequest: Boolean) {
        binding.viewModel?.getLatestArticles(context!!, isPullToRefreshRequest, section, period)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem!=null) {
            val searchView = searchItem.actionView as SearchView
            val searchHint = getString(R.string.search_hint)
            searchView.setQueryHint(searchHint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.viewModel?.filterNews(query!!.toString())
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    binding.viewModel?.filterNews(query!!.toString())
                    return false
                }
            })
        }
    }

    //handle item clicks of menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_period_1) {
            period = "1"
            getLiveData(true)
        } else if (id == R.id.action_period_7) {
            period = "7"
            getLiveData(true)
        } else if (id == R.id.action_period_30) {
            period = "30"
            getLiveData(true)
        }

        return super.onOptionsItemSelected(item)
    }
}
