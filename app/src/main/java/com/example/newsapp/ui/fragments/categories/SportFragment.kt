package com.example.newsapp.ui.fragments.categories

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
import com.example.newsapp.databinding.FragmentSportBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.viewmodels.CategoriesViewModel
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class SportFragment : Fragment(R.layout.fragment_sport) {
lateinit var binding : FragmentSportBinding
 val newsAdapter = NewsAdapter()
     val categoriesViewModel by viewModels<CategoriesViewModel>()
     val newsViewModel by viewModels<NewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSportBinding.bind(view)

        setupRecyclerView()
        categoriesViewModel.getCategoryNews("sports")
        //newsViewModel.getHeadlines("sports")

        if (categoriesViewModel.internetConnection((activity as NewsActivity).applicationContext)){
            lifecycleScope.launch {
                categoriesViewModel.sportNews.observe(viewLifecycleOwner, Observer {

                    when(it){
                        is Resources.Success<*> ->{
                            hidePR()
                            it.data?.let {

                                val firstList = it.results.toList()

                                newsAdapter.differ.submitList(firstList)
                            }
                        }
                        is Resources.Error -> {
                             hidePR()
                            it.message?.let {message ->
                                Toast.makeText(activity," Sorry Erro $message", Toast.LENGTH_LONG).show()
                                //  showErrorMessage(message)
                            }
                        }
                        is Resources.Loading -> {
                           showPR()
                        }
                    }
                })
            }
        }

        handleArticleClick()

    }

    fun setupRecyclerView(){
        binding.sportRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

    fun showPR(){
        binding.SportProgressBar.visibility = View.VISIBLE
    }
    fun hidePR(){
        binding.SportProgressBar.visibility = View.GONE
    }

    fun handleArticleClick(){
        newsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment,bundle)
        }
    }
}