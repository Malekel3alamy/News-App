package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentFavouritesBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favourites) {

    val newsViewModel by viewModels<NewsViewModel>()

    lateinit var newsAdapter: NewsAdapter
    lateinit var binding : FragmentFavouritesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)



        setUpFavouritesRecycler()
        // Setting articles list for the adapter
        if (newsViewModel.roomArticles != null){
            lifecycleScope.launch {
                newsViewModel.roomArticles!!.observe(viewLifecycleOwner, Observer {
                        articles ->

                    newsAdapter.differ.submitList(articles)

                })
            }
        }

        newsAdapter.setOnClickListener {
            val bundle =Bundle().apply {

                putParcelable("article",it)
            }

        }

 val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
  ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
     override fun onMove(
         recyclerView: RecyclerView,
         viewHolder: RecyclerView.ViewHolder,
         target: RecyclerView.ViewHolder
     ): Boolean {
         return  true
     }

     override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

         val position = viewHolder.adapterPosition
         val article = newsAdapter.differ.currentList[position]
         newsViewModel.deleteArticle(article)
         Snackbar.make(view," Article Deleted ",Snackbar.LENGTH_LONG)
             .setAction(" Undo"){
                 newsViewModel.addToFavourite(article)
             }.show()
     }
 }

     ItemTouchHelper(itemTouchHelperCallback).apply {
         attachToRecyclerView(binding.recyclerFavourites)
     }


    }


    private fun setUpFavouritesRecycler() {

        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply {


            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }




}