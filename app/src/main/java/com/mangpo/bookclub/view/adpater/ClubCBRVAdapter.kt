package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemClubCbBinding
import com.mangpo.bookclub.model.entities.ClubFilterEntity

class ClubCBRVAdapter(): RecyclerView.Adapter<ClubCBRVAdapter.ClubCBViewHolder>() {
    private var clubs: ArrayList<ClubFilterEntity> = arrayListOf()
    private var clickedClubs: ArrayList<Int> = arrayListOf()

    private lateinit var binding: ItemClubCbBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClubCBRVAdapter.ClubCBViewHolder {
        binding = ItemClubCbBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClubCBViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClubCBRVAdapter.ClubCBViewHolder, position: Int) {
        holder.clubCB.text = clubs[position].name
        holder.clubCB.isChecked = clubs[position].isChecked
        holder.clubCB.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                clickedClubs.add(clubs[position].clubId)
            else
                clickedClubs.remove(clubs[position].clubId)
        }
    }

    override fun getItemCount(): Int = clubs.size

    inner class ClubCBViewHolder(itemView: ItemClubCbBinding): RecyclerView.ViewHolder(itemView.root) {
        val clubCB: CheckBox = itemView.clubCb
    }

    fun setData(clubs: ArrayList<ClubFilterEntity>) {
        this.clubs = clubs
        notifyDataSetChanged()
    }

    fun getClickedClubs(): ArrayList<Int> = clickedClubs

    fun setCheckedUI(checkedClubId: ArrayList<Int>) {
        for (club in clubs) {
            club.isChecked = checkedClubId.contains(club.clubId)
        }
        notifyDataSetChanged()
    }
}