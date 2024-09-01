package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.newsapp.adapters.RoomNewsAdapter
import com.example.newsapp.databinding.FragmentFavouritesBinding
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favourites) {

    private val newsViewModel by viewModels<NewsViewModel>()

     private val roomNewsAdapter = RoomNewsAdapter()
    private lateinit var binding : FragmentFavouritesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)

        setUpFavouritesRecycler()
        // Setting articles list for the adapter

            lifecycleScope.launch {
                newsViewModel.getFavouriteNews()
                newsViewModel.roomArticles.collectLatest {
                    when (it) {
                        is Resources.Error -> {
                            hideProgressBar()
                            Toast.makeText(requireContext(), " ${it.message}", Toast.LENGTH_LONG)
                                .show()
                        }

                        is Resources.Loading -> {
                            showProgressBar()
                        }

                        is Resources.Success -> {
                            hideProgressBar()
                            roomNewsAdapter.roomDiffer.submitList(it.data)
                            Log.d(
                                "Listsize",
                                roomNewsAdapter.roomDiffer.currentList.size.toString()
                            )
                        }
                    }

                }
            }


        roomNewsAdapter.onItemClickListener ={
            val bundle = Bundle().apply {

                putParcelable("article", it)
            }
            findNavController().navigate(R.id.action_favouritesFragment_to_articleFragment,bundle)

        }
        roomNewsAdapter.onDeleteImageClickListener={
            newsViewModel.deleteArticle(it)
            roomNewsAdapter.notifyDataSetChanged()
        }

    }

    private fun setUpFavouritesRecycler() {

        binding.recyclerFavourites.apply {
            adapter = roomNewsAdapter
            layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        }
    }
    private fun hideProgressBar(){
        binding.bookmarksProgressBar.visibility = View.GONE

    }

    private fun showProgressBar(){

        binding.bookmarksProgressBar.visibility = View.VISIBLE

    }



}