package com.example.newsapp.ui.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentSportBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.fragments.categories.viewmodels.CategoriesViewModel
import com.example.newsapp.ui.fragments.categories.viewmodels.NewsViewModel
import com.example.newsapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessFragment : Fragment(R.layout.fragment_sport) {
    lateinit var binding : FragmentSportBinding
    private val newsAdapter = NewsAdapter()
    private val categoriesViewModel by viewModels<CategoriesViewModel>()
    private val newsViewModel by viewModels<NewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSportBinding.bind(view)

        setupRecyclerView()
        // categoriesViewModel.getHeadlines("sports")
        newsViewModel.getHeadlines("business")

        if (categoriesViewModel.internetConnection((activity as NewsActivity).applicationContext)){
            lifecycleScope.launch {
                newsViewModel.headlines.observe(viewLifecycleOwner, Observer {

                    when(it){
                        is Resources.Success<*> ->{
                            it.data?.let {

                                val firstList = it.results.toList()

                                newsAdapter.differ.submitList(firstList)
                            }
                        }
                        is Resources.Error -> {

                            it.message?.let {message ->
                                Toast.makeText(activity," Sorry Erro $message", Toast.LENGTH_LONG).show()
                                //  showErrorMessage(message)
                            }
                        }
                        is Resources.Loading -> {

                        }
                    }
                })
            }
        }

    }

    fun setupRecyclerView(){
        binding.sportRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }
}