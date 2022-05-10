package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemInviteHistoryBinding
import com.mangpo.bookclub.model.remote.Invite

class InviteHistoryRVAdapter(): RecyclerView.Adapter<InviteHistoryRVAdapter.InviteHistoryViewHolder>() {
    private var inviteHistories: List<Invite> = listOf()

    private lateinit var binding: ItemInviteHistoryBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InviteHistoryRVAdapter.InviteHistoryViewHolder {
        binding = ItemInviteHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return InviteHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: InviteHistoryRVAdapter.InviteHistoryViewHolder,
        position: Int
    ) {
        holder.messageTv.text = inviteHistories[position].message
    }

    override fun getItemCount(): Int = inviteHistories.size

    fun setData(inviteHistories: List<Invite>) {
        this.inviteHistories = inviteHistories
    }

    inner class InviteHistoryViewHolder(itemView: ItemInviteHistoryBinding): RecyclerView.ViewHolder(itemView.root) {
        val profileIv: ImageView = itemView.inviteHistoryProfileIv
        val nicknameTv: TextView = itemView.inviteHistoryNicknameTv
        val bookclubNameTv: TextView = itemView.inviteHistoryBookclubTv
        val inviteDateTv: TextView = itemView.inviteHistoryInviteDateTv
        val messageTv: TextView = itemView.inviteHistoryMessageTv
    }
}
