package com.example.newsapp.ui.fragments.categories

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentSportBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.fragments.CategoriesFragment
import com.example.newsapp.ui.viewmodels.CategoriesViewModel
import com.example.newsapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class MainCategoryFragment : Fragment(R.layout.fragment_sport) {

lateinit var binding : FragmentSportBinding
 val newsAdapter = NewsAdapter()
    val categoriesViewModel by viewModels<CategoriesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSportBinding.bind(view)



    }

    fun setupRecyclerView(){
        binding.newsRV.apply {
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
            findNavController().navigate(R.id.action_categoriesFragment_to_articleFragment,bundle)
        }
    }
}