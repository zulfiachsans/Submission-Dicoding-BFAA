package com.dicoding.submissiondua.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissiondua.adapter.UserAdapter
import com.dicoding.submissiondua.databinding.FragmentFollowBinding
import com.dicoding.submissiondua.model.User
import com.dicoding.submissiondua.viewmodel.FollowViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FollowFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var followViewModel: FollowViewModel
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!


    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(index: Int) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        showLoading(true)
        showRecyclerView()

        followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)

        val user : User = activity?.intent?.getParcelableExtra(DetailActivity.EXTRA_USER)!!
        Log.d("USER", user.toString())
        when (index) {
            1 -> user.followers_url?.let { followViewModel.setUser(it) }
            else -> user.following_url?.let { followViewModel.setUser(it) }
        }

        followViewModel.getUser().observe(viewLifecycleOwner, { listUser ->
            if (listUser.isNullOrEmpty()) {
                binding.tvNotfound.visibility = View.VISIBLE
            } else {
                binding.tvNotfound.visibility = View.GONE
            }

            showLoading(false)
            adapter.setUser(listUser)
        })
    }

    private fun showRecyclerView() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        val rv = binding.recycleView
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapter
        rv.setHasFixedSize(true)
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvNotfound.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}