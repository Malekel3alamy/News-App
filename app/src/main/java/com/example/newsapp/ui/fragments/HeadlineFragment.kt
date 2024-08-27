package com.example.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.utils.Constants
import com.example.newsapp.databinding.FragmentHeadlineBinding
import com.example.newsapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadlineFragment : Fragment(R.layout.fragment_headline) {

     val newsViewModel by viewModels<NewsViewModel>()
    private lateinit var newsAdapter : NewsAdapter
    private lateinit var binding : FragmentHeadlineBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHeadlineBinding.bind(view)

        setUpHeadlinesRecycler()

        if (newsViewModel.internetConnection((activity as NewsActivity).applicationContext)){
            lifecycleScope.launch {
                newsViewModel.headlines.observe(viewLifecycleOwner, Observer {

                    when(it){
                        is Resources.Success<*> ->{
                            hideProgressBar()
                            // hideErrorMessage()
                            it.data?.let {
                                newsAdapter.differ.submitList(it.results.toList())
                                val totalPages = it.totalResults / com.example.newsapp.utils.Constants.QUERY_PAGE_SIZE +2
                                isLastPage = newsViewModel.headlinesPage == totalPages
                                if(isLastPage){
                                    binding.recyclerHeadlines.setPadding(0,0,0,0)
                                }
                            }
                        }
                        is Resources.Error -> {
                            hideProgressBar()
                            it.message?.let {message ->
                                Toast.makeText(activity," Sorry Erro $message",Toast.LENGTH_LONG).show()
                                //  showErrorMessage(message)
                            }
                        }
                        is Resources.Loading -> {
                            showProgressBar()
                        }
                    }
                })
            }
        }
     newsAdapter.setOnClickListener {
    val bundle = Bundle().apply {
        putParcelable("article",it)
      }
         findNavController().navigate(R.id.action_headlineFragment_to_articleFragment,bundle)
    }


      /*  retryButton.setOnClickListener {

            newsViewModel.getHeadlines()
        }*/
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar(){

        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    /*private fun hideErrorMessage(){
        itemHeadlinesError.visibility = View.INVISIBLE
        isError = false
    }*/
  /*  private fun showErrorMessage(message: String){
        itemHeadlinesError.visibility = View.VISIBLE
        isError = true
        errorText.text = message
    }*/

    val scrollListener  = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanvisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage
                    && isNotAtBeginning && isTotalMoreThanvisible && isScrolling
            if (shouldPaginate) {

                isScrolling = false

            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)


            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                isScrolling = true
            }
        }
    }

        private fun setUpHeadlinesRecycler(){

            newsAdapter = NewsAdapter()
            binding.recyclerHeadlines.apply {


           adapter =  newsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addOnScrollListener(this@HeadlineFragment.scrollListener)
           }

        }

}