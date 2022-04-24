package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemClubVerSmallBinding
import com.mangpo.bookclub.model.entities.ClubShortEntity

class ClubVerSmallRVAdapter(private val role: String): RecyclerView.Adapter<ClubVerSmallRVAdapter.ClubVerSmallViewHolder>() {
    private var clubs: ArrayList<ClubShortEntity> = arrayListOf()

    private lateinit var myClickListener: MyClickListener
    private lateinit var binding: ItemClubVerSmallBinding

    interface MyClickListener {
        fun delete(clubId: Int)
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
        holder.clubName.text = clubs[position].name
        holder.createDate.text = clubs[position].createDate

        if (role=="PRES")
            holder.deleteBtn.text = "삭제하기"
        else
            holder.deleteBtn.text = "탈퇴하기"

        holder.deleteBtn.setOnClickListener {
            myClickListener.delete(clubs[position].clubId)
        }
    }

    override fun getItemCount(): Int = clubs.size

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun setData(clubs: ArrayList<ClubShortEntity>) {
        this.clubs = clubs
        notifyDataSetChanged()
    }

    inner class ClubVerSmallViewHolder(itemView: ItemClubVerSmallBinding): RecyclerView.ViewHolder(itemView.root) {
        val clubName: TextView = itemView.bookClubNameTv
        val createDate: TextView = itemView.bookClubDateTv
        val deleteBtn: Button = itemView.deleteBtn
    }
}