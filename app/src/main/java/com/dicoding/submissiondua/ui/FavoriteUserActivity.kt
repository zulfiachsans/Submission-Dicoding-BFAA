package com.dicoding.submissiondua.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissiondua.R
import com.dicoding.submissiondua.adapter.UserAdapter
import com.dicoding.submissiondua.databinding.ActivityFavoriteUserBinding
import com.dicoding.submissiondua.mapping.MappingHelper
import com.dicoding.submissiondua.model.User
import com.dicoding.submissiondua.viewmodel.FavoriteViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: UserAdapter
    private lateinit var userFavoriteViewModel: FavoriteViewModel
    private lateinit var userFav: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userFavoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        showLoading(true)
    }

    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()

        val getFavorite = userFavoriteViewModel.getFavorites()
        userFav = MappingHelper.mapCursorToArrayList(getFavorite)
        if (userFav.isNullOrEmpty()) {
            binding.image.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.VISIBLE
        } else {
            binding.image.visibility = View.GONE
            binding.tvNotFound.visibility = View.GONE
        }

        showLoading(false)
        showRecyclerView()
        adapter.setUser(userFav as ArrayList<User>)
    }

    @InternalCoroutinesApi
    private fun showRecyclerView() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        val rv = binding.recycleView
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        rv.setHasFixedSize(true)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                showSelectedUser(user)
            }
        })
    }

    @InternalCoroutinesApi
    private fun showSelectedUser(user: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(intent)
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}