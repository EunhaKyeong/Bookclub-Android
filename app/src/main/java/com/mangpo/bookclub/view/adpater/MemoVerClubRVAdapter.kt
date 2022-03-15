package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemMemoVerClubBinding

class MemoVerClubRVAdapter(): RecyclerView.Adapter<MemoVerClubRVAdapter.MemoVerClubViewHolder>() {

    private lateinit var binding: ItemMemoVerClubBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoVerClubRVAdapter.MemoVerClubViewHolder {
        binding = ItemMemoVerClubBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MemoVerClubViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MemoVerClubRVAdapter.MemoVerClubViewHolder,
        position: Int
    ) {
    }

    override fun getItemCount(): Int = 3

    inner class MemoVerClubViewHolder(itemView: ItemMemoVerClubBinding): RecyclerView.ViewHolder(itemView.root) {

    }
}