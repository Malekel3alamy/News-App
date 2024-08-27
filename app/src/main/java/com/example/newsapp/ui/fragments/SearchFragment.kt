package com.example.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.InvalidationTracker
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter : NewsAdapter
    lateinit var retryButton : Button
    lateinit var errorText : TextView
    lateinit var itemSearchError: CardView
    lateinit var binding : FragmentSearchBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          binding = FragmentSearchBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel

        itemSearchError = view.findViewById(R.id.itemSearchError)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ite_view_error = inflater.inflate(R.layout.item_error,null)

        retryButton = ite_view_error.findViewById(R.id.retryButton)
        errorText = ite_view_error.findViewById(R.id.errorText)


        setUpSearchRecycler()
      /*  newsAdapter.setOnItemClickListener {article ->
            Log.d("Articel","$article")
            val bundle = Bundle().apply{
                article.id = 2
                article.content=""
                article.description=""
                article.urlToImage = ""
                article.author = ""
                article.source?.id =""
                putParcelable("article",article)
                Log.d("Article","$article")
            }
            if(findNavController().currentDestination?.id == R.id.searchFragment && bundle!= null ){

                findNavController().navigate(R.id.action_searchFragment_to_articleFragment,bundle)

            }else{
                Log.d("ItemClick","Error")
            }
        }*/

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
                    hideProgressBar()
                    hideErrorMessage()
                    it.data?.let {
                        newsAdapter.differ.submitList(it.results.toList())
                        val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        if (isLastPage) {
                            binding.recyclerSearch.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resources.Error -> {
                    hideProgressBar()
                    it.message?.let { message ->
                        Toast.makeText(activity, " Sorry Erro $message", Toast.LENGTH_SHORT).show()
                        showErrorMessage(message)
                    }


                }

                is Resources.Loading -> {
                    showProgressBar()

                }
            }
        })

        retryButton.setOnClickListener {
            if(binding.searchEdit.text.toString().isNotEmpty()){
                newsViewModel.getSearchNews(binding.searchEdit.text.toString())
            }else{
                hideErrorMessage()
            }
        }



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
    private fun hideErrorMessage(){
        itemSearchError.visibility = View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(message: String){
        itemSearchError.visibility = View.VISIBLE
        isError = true
        errorText.text = message
    }

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
                newsViewModel.getSearchNews(binding.searchEdit.text.toString())
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
    private fun setUpSearchRecycler(){

        newsAdapter = NewsAdapter()
        binding.recyclerSearch.apply {


            adapter =  newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }

    }
}