package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.adapters.TopHeadlinesAdapter
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

     val newsViewModel by viewModels<NewsViewModel>()
    private lateinit var newsAdapter : NewsAdapter
    private lateinit var topHeadlinesAdapter: TopHeadlinesAdapter
    private lateinit var binding : FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        setUpHeadlinesRecycler()
        setUpTopHeadlinesRecycler()



       newsViewModel.getHeadlines("top")
        if (newsViewModel.internetConnection((activity as NewsActivity).applicationContext)){
            lifecycleScope.launch {
                newsViewModel.headlines.observe(viewLifecycleOwner, Observer {

                    when(it){
                        is Resources.Success<*> ->{
                            hideProgressBar()
                            // hideErrorMessage()
                            it.data?.let {


                                val nextPageList = it.results.toList()
                                val newsList = nextPageList.subList(1,it.results.size)
                                newsAdapter.differ.submitList(newsList)
                                topHeadlinesAdapter.differ.submitList(mutableListOf(it.results[0]))
                              //  newsAdapter.differ.currentList.addAll(newsAdapter.differ.currentList.size +1,it.results.toList())
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





        topHeadlinesAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(R.id.action_headlineFragment_to_articleFragment,bundle)
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


    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar(){

        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
  /*  private fun hideErrorMessage(){
        itemHeadlinesError.visibility = View.INVISIBLE
        isError = false
    }*/
  /*  private fun showErrorMessage(message: String){
        itemHeadlinesError.visibility = View.VISIBLE
        isError = true
        errorText.text = message
    }*/


        private fun setUpHeadlinesRecycler(){

            newsAdapter = NewsAdapter()
            binding.recyclerHeadlines.apply {


           adapter =  newsAdapter
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

           }

        }

    private fun setUpTopHeadlinesRecycler(){

        topHeadlinesAdapter = TopHeadlinesAdapter()
        binding.recyclerTopHeadlines.apply {


            adapter =  topHeadlinesAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }

    }

}