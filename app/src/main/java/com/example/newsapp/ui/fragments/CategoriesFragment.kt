package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.example.newsapp.R
import com.example.newsapp.adapters.HomeViewPagerAdapter
import com.example.newsapp.databinding.FragmentCategoriesBinding
import com.example.newsapp.ui.fragments.categories.BusinessFragment
import com.example.newsapp.ui.fragments.categories.EntertainmentFragment
import com.example.newsapp.ui.fragments.categories.MainCategoryFragment
import com.example.newsapp.ui.fragments.categories.SportFragment
import com.example.newsapp.ui.fragments.categories.TechnologiesFragment
import com.example.newsapp.ui.viewmodels.CategoriesViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class CategoriesFragment : Fragment(R.layout.fragment_categories) {


    lateinit var binding : FragmentCategoriesBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          binding = FragmentCategoriesBinding.bind(view)

        val categoriesFragments = arrayListOf<Fragment>(
            TechnologiesFragment(),
            EntertainmentFragment(),
            SportFragment(),
            BusinessFragment()
        )

        val homeViewPagerAdapter =
            HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.viewPagerHome.adapter = homeViewPagerAdapter

        TabLayoutMediator(binding.tableLayout,binding.viewPagerHome){tab ,position ->

            when(position){
                0 -> tab.text = "Technologies"
                1 -> tab.text = "Entertainment"
                2 -> tab.text = "Sport"
                3 -> tab.text = "Business"
            }
        }.attach()

    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.GONE

    }

    private fun showProgressBar(){

        binding.paginationProgressBar.visibility = View.VISIBLE

    }






}