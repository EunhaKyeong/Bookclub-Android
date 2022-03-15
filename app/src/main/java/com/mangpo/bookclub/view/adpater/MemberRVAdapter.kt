package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemMemberBinding

class MemberRVAdapter(): RecyclerView.Adapter<MemberRVAdapter.MemberViewHolder>() {
    interface MyClickListener {
        fun goMemberInfoView()
    }

    private lateinit var binding: ItemMemberBinding
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemberRVAdapter.MemberViewHolder {
        binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberRVAdapter.MemberViewHolder, position: Int) {
        holder.root.setOnClickListener {
            myClickListener.goMemberInfoView()
        }
    }

    override fun getItemCount(): Int = 10

    inner class MemberViewHolder(itemView: ItemMemberBinding): RecyclerView.ViewHolder(itemView.root) {
        val root: ConstraintLayout = itemView.root
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }
}