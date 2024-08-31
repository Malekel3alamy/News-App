package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    lateinit var binding: FragmentSearchBinding

    val newsViewModel by viewModels<NewsViewModel>()
    lateinit var newsAdapter : NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        setUpSearchRecycler()

        var job: Job? = null
        binding.searchEdit.addTextChangedListener(){ editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString() .isNotEmpty()){
                        newsViewModel.getSearchNews(editable.toString())

                    }
                }

            }

        }

        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer {

            when (it) {

                is Resources.Success<*> -> {
                       hidePR()
                    hideSearchImage()
                    it.data?.let {

                        newsAdapter.differ.submitList(it.results.toList())
                    }
                }

                is Resources.Error -> {
                    hidePR()
                     showSearchImage()
                    it.message?.let { message ->
                        Toast.makeText(activity, " Sorry Erro $message", Toast.LENGTH_SHORT).show()

                    }


                }

                is Resources.Loading -> {
                    hideSearchImage()
                   showPR()

                }
            }
        })

        newsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment,bundle)
        }
    }

    private fun setUpSearchRecycler(){

        newsAdapter = NewsAdapter()
        binding.recyclerSearch.apply {


            adapter =  newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }

    }

    private fun showPR(){
        binding.searchProgressBar.visibility = View.VISIBLE
    }
    private fun hidePR(){
        binding.searchProgressBar.visibility = View.GONE
    }

    private fun showSearchImage(){
        binding.searchImage.visibility = View.VISIBLE
        binding.recyclerSearch.visibility = View.GONE
    }
    private fun hideSearchImage(){
        binding.searchImage.visibility = View.GONE
        binding.recyclerSearch.visibility = View.VISIBLE
    }


}