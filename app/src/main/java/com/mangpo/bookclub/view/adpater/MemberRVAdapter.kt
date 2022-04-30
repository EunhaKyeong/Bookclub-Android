package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.ItemMemberBinding
import com.mangpo.bookclub.model.entities.ClubMemberDetail

class MemberRVAdapter(): RecyclerView.Adapter<MemberRVAdapter.MemberViewHolder>() {
    interface MyClickListener {
        fun goMemberInfoView(memberId: Int)
    }

    private lateinit var members: ArrayList<ClubMemberDetail>
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
        Glide.with(holder.itemView).load(members[position].profile).circleCrop().into(holder.profile)
        holder.nickname.text = members[position].nickname
        holder.introduce.text = members[position].introduce

        if (position==0) {
            holder.presRole.visibility = View.VISIBLE
            holder.memRole.visibility = View.INVISIBLE
        } else {
            holder.presRole.visibility = View.INVISIBLE
            holder.memRole.visibility = View.VISIBLE
        }

        holder.root.setOnClickListener {
            myClickListener.goMemberInfoView(members[position].memberId!!)
        }
    }

    override fun getItemCount(): Int = members.size

    inner class MemberViewHolder(itemView: ItemMemberBinding): RecyclerView.ViewHolder(itemView.root) {
        val root: ConstraintLayout = itemView.root
        val profile: ImageView = itemView.userIv
        val nickname: TextView = itemView.nicknameTv
        val presRole: TextView = itemView.clubPresTv
        val memRole: TextView = itemView.clubMemTv
        val introduce: TextView = itemView.introduceTv
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun setData(members: ArrayList<ClubMemberDetail>) {
        this.members = members
    }
}