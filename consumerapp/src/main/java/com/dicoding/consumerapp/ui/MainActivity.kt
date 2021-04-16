package com.dicoding.consumerapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.adapter.UserAdapter
import com.dicoding.consumerapp.databinding.ActivityMainBinding
import com.dicoding.consumerapp.mapping.MappingHelper
import com.dicoding.consumerapp.model.User
import com.dicoding.consumerapp.viewmodel.FavoriteViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var userFav: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Consumer App"

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        showLoading(true)
    }

    override fun onResume() {
        super.onResume()

        val getFavorite = favoriteViewModel.getFavorites()
        userFav = MappingHelper.mapCursorToArrayList(getFavorite)
        if (userFav.isNullOrEmpty()) {
            binding.imageView.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.VISIBLE
        } else {
            binding.imageView.visibility = View.GONE
            binding.tvNotFound.visibility = View.GONE
        }

        showLoading(false)
        showRecyclerView()
        adapter.setUser(userFav as ArrayList<User>)
    }

    private fun showRecyclerView() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        val rv = binding.recycleView
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        rv.setHasFixedSize(true)
    }


    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}