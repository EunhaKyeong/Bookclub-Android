package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemBookVerClubBinding
import com.mangpo.bookclub.model.remote.BookInClub

class BookVerClubRVAdapter(): RecyclerView.Adapter<BookVerClubRVAdapter.BookVerClubViewHolder>() {
    private var books: List<BookInClub> = listOf()

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
        holder.bookName.text = books[position].bookName
        holder.bookIv.clipToOutline = true
        if (books[position].bookImg==null)
            holder.bookIv.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.default_book))
        else
            Glide.with(binding.root.context).load(books[position].bookImg).into(holder.bookIv)
        holder.nickname.text = books[position].userNickname

    }

    override fun getItemCount(): Int = books.size

    fun setData(books: List<BookInClub>) {
        this.books = books
        notifyDataSetChanged()
    }

    inner class BookVerClubViewHolder(itemView: ItemBookVerClubBinding): RecyclerView.ViewHolder(itemView.root) {
        val bookName: TextView = itemView.bookNameTv
        val bookIv: ImageView = itemView.bookIv
        val nickname: TextView = itemView.nicknameTv
    }
}