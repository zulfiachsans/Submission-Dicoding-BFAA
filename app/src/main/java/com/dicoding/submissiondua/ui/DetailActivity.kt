package com.dicoding.submissiondua.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.dicoding.submissiondua.R
import com.dicoding.submissiondua.adapter.SectionPagerAdapter
import com.dicoding.submissiondua.databinding.ActivityDetailBinding
import com.dicoding.submissiondua.local.DatabaseUser
import com.dicoding.submissiondua.local.FavoriteUserDao
import com.dicoding.submissiondua.mapping.MappingHelper.mapCursorToObject
import com.dicoding.submissiondua.model.DetailUser
import com.dicoding.submissiondua.model.User
import com.dicoding.submissiondua.viewmodel.DetailUserViewModel
import com.dicoding.submissiondua.viewmodel.FavoriteViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var database: DatabaseUser
    private lateinit var dao: FavoriteUserDao

    private var isFavorite: Boolean = false
    private var username = ""

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val user : User = intent.getParcelableExtra(EXTRA_USER)!!
        username = user.username

        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        database = DatabaseUser.getInstance(applicationContext)
        dao = database.favoriteDao()
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()


        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        user.detail_url?.let { detailUserViewModel.setDetailUser(it) }

        detailUserViewModel.getDetailUser().observe(this, { userDetail ->
            if (userDetail != null) {
                showDetailUser(userDetail)
                showLoading(false)
            }
        })

        btnFavoriteChecked(username)
        binding.buttonFavorite.setOnClickListener {
            isFavorite = !isFavorite
            when (isFavorite) {
                true -> insertFavorite(user)
                false -> deleteFavorite(user)
            }
        }
    }

    private fun btnFavoriteChecked(username: String) {
        isFavorite =
            (favoriteViewModel.getFavoriteByUsername(username)?.mapCursorToObject() != null)
        if (isFavorite) {
            binding.buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun insertFavorite(user: User) {
        favoriteViewModel.insertFavorite(user)
        binding.buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        Toast.makeText(this, getString(R.string.insert_success), Toast.LENGTH_SHORT).show()
    }

    private fun deleteFavorite(user: User) {
        favoriteViewModel.deleteFavorite(user.username)
        binding.buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show()
    }

    private fun showDetailUser(userDetail: DetailUser) {
        Glide.with(this)
            .load(userDetail.avatar)
            .into(binding.includeHeader.imgDetailAvatar)
        binding.includeHeader.detailUserRepositories.text = userDetail.repository.toString()
        binding.includeHeader.detailUserFollowers.text = userDetail.followers.toString()
        binding.includeHeader.detailUserFollowing.text = userDetail.following.toString()

        if(userDetail.bio == "null"){
            binding.detailUserBio.visibility = View.GONE
        }
        else{
            binding.detailUserBio.text = userDetail.bio
        }
        if (userDetail.company == "null") {
            binding.detailUserCompany.visibility = View.GONE
        } else {
            binding.detailUserCompany.text = userDetail.company
        }
        if (userDetail.location == "null") {
            binding.detailUserLocation.visibility = View.GONE
        } else {
            binding.detailUserLocation.text = userDetail.location
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.includeHeader.imgDetailAvatar.visibility = View.GONE
            binding.includeHeader.detailUserRepositories.visibility = View.GONE
            binding.includeHeader.detailUserFollowers.visibility = View.GONE
            binding.includeHeader.detailUserFollowing.visibility = View.GONE
            binding.includeHeader.repo.visibility = View.GONE
            binding.includeHeader.followers.visibility = View.GONE
            binding.includeHeader.following.visibility = View.GONE
            binding.detailUserBio.visibility = View.GONE
            binding.detailUserCompany.visibility = View.GONE
            binding.detailUserLocation.visibility = View.GONE
            binding.tabs.visibility = View.GONE
            binding.viewPager.visibility = View.GONE

            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.includeHeader.imgDetailAvatar.visibility = View.VISIBLE
            binding.includeHeader.detailUserRepositories.visibility = View.VISIBLE
            binding.includeHeader.detailUserFollowers.visibility = View.VISIBLE
            binding.includeHeader.detailUserFollowing.visibility = View.VISIBLE
            binding.includeHeader.repo.visibility = View.VISIBLE
            binding.includeHeader.followers.visibility = View.VISIBLE
            binding.includeHeader.following.visibility = View.VISIBLE
            binding.detailUserBio.visibility = View.VISIBLE
            binding.detailUserCompany.visibility = View.VISIBLE
            binding.detailUserLocation.visibility = View.VISIBLE
            binding.tabs.visibility = View.VISIBLE
            binding.viewPager.visibility = View.VISIBLE

            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}