package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemBookVerClubBinding

class BookVerClubRVAdapter(): RecyclerView.Adapter<BookVerClubRVAdapter.BookVerClubViewHolder>() {

    private lateinit var binding: ItemBookVerClubBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookVerClubRVAdapter.BookVerClubViewHolder {
        binding = ItemBookVerClubBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookVerClubViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BookVerClubRVAdapter.BookVerClubViewHolder,
        position: Int
    ) {
    }

    override fun getItemCount(): Int = 8

    inner class BookVerClubViewHolder(itemView: ItemBookVerClubBinding): RecyclerView.ViewHolder(itemView.root) {

    }
}