package com.dicoding.submissiondua.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissiondua.databinding.ItemRowBinding
import com.dicoding.submissiondua.model.DetailUser
import com.dicoding.submissiondua.model.User
import kotlinx.coroutines.InternalCoroutinesApi


class UserAdapter : RecyclerView.Adapter<UserAdapter.RecyclerViewHolder>() {

    private val listUser = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setUser(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    class RecyclerViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(imgDetailAvatar)
                idUsername.text = user.username
                idType.text = user.tipe
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(listUser[position])
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(listUser[position]) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}