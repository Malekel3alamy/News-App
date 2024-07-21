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
import androidx.lifecycle.Observer
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


class HeadlineFragment : Fragment(R.layout.fragment_headline) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter : NewsAdapter
    lateinit var retryButton : Button
    lateinit var errorText : TextView
    lateinit var itemHeadlinesError:CardView
    lateinit var binding : FragmentHeadlineBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHeadlineBinding.bind(view)
        newsViewModel = (activity as NewsActivity).newsViewModel

        itemHeadlinesError = view.findViewById(R.id.itemHeadlinesError)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ite_view_error = inflater.inflate(R.layout.item_error,null)

        retryButton = ite_view_error.findViewById(R.id.retryButton)
        errorText = ite_view_error.findViewById(R.id.errorText)

        newsViewModel.getHeadlines("us")

        setUpHeadlinesRecycler()


        if (newsViewModel.internetConnection((activity as NewsActivity).applicationContext)){
            newsViewModel.headlines.observe(viewLifecycleOwner, Observer {

                when(it){

                    is com.example.newsapp.utils.Resources.Success<*> ->{
                        hideProgressBar()
                        hideErrorMessage()
                        it.data?.let {
                            newsAdapter.differ.submitList(it.articles.toList())
                            val totalPages = it.totalResults / com.example.newsapp.utils.Constants.QUERY_PAGE_SIZE +2
                            isLastPage = newsViewModel.headlinesPage == totalPages
                            if(isLastPage){
                                binding.recyclerHeadlines.setPadding(0,0,0,0)
                            }
                        }
                    }

                    is com.example.newsapp.utils.Resources.Error -> {
                        hideProgressBar()
                        it.message?.let {message ->
                            Toast.makeText(activity," Sorry Erro $message",Toast.LENGTH_SHORT).show()
                            showErrorMessage(message)
                        }


                    }
                    is com.example.newsapp.utils.Resources.Loading -> {
                        showProgressBar()

                    }
                }
            })

        }

        newsAdapter.setOnItemClickListener {article ->
            val bundle = Bundle().apply{
                article.id = 1
                article.content=""
                article.description=""
                article.urlToImage = ""
                putParcelable("article",article)
                Log.d("Article","$article")
            }
            if(findNavController().currentDestination?.id == R.id.headlineFragment && bundle!= null ){

             findNavController().navigate(R.id.action_headlineFragment_to_articleFragment,bundle)

            }else{
                Log.d("ItemClick","Error")
            }
        }

        retryButton.setOnClickListener {

            newsViewModel.getHeadlines("us")
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
        itemHeadlinesError.visibility = View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(message: String){
        itemHeadlinesError.visibility = View.VISIBLE
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
                newsViewModel.getHeadlines("us")
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
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@HeadlineFragment.scrollListener)
           }

        }

}