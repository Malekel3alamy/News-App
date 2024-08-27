package com.example.newsapp.ui

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.SavedFragment
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.ui.fragments.FavoritesFragment
import com.example.newsapp.ui.fragments.HeadlineFragment
import com.example.newsapp.ui.fragments.SearchFragment


class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel

    private lateinit var binding: ActivityNewsBinding
     private lateinit var navController: NavController
     val headlinesFragment = HeadlineFragment()
    val searchFragment = SearchFragment()
    val favouritesFragment = FavoritesFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepo = NewsRepo(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepo)
        newsViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.news_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


      //  nav.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
               R.id.headlineFragment -> setCurrentFragment(headlinesFragment)
               R.id.favouritesFragment -> setCurrentFragment(favouritesFragment)
               R.id.searchFragment -> setCurrentFragment(searchFragment)
            }
            true
        }

    }

    private fun setCurrentFragment (fragment : Fragment): Boolean {

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.news_nav_host_fragment,fragment)
            commit()
        }
        return true
    }

}






