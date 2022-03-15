package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemClubVerSmallBinding

class ClubVerSmallRVAdapter(private val role: String): RecyclerView.Adapter<ClubVerSmallRVAdapter.ClubVerSmallViewHolder>() {
    private lateinit var myClickListener: MyClickListener
    private lateinit var binding: ItemClubVerSmallBinding

    interface MyClickListener {
        fun delete(role: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClubVerSmallRVAdapter.ClubVerSmallViewHolder {
        binding = ItemClubVerSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClubVerSmallViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ClubVerSmallRVAdapter.ClubVerSmallViewHolder,
        position: Int
    ) {
        if (role=="PRES")
            holder.deleteBtn.text = "삭제하기"
        else
            holder.deleteBtn.text = "탈퇴하기"

        holder.deleteBtn.setOnClickListener {
            myClickListener.delete(role)
        }
    }

    override fun getItemCount(): Int = 3

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    inner class ClubVerSmallViewHolder(itemView: ItemClubVerSmallBinding): RecyclerView.ViewHolder(itemView.root) {
        val deleteBtn: Button = itemView.deleteBtn
    }
}