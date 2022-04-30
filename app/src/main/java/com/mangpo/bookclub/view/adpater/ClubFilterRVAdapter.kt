package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemClubFilterBinding
import com.mangpo.bookclub.model.entities.ClubFilterEntity

class ClubFilterRVAdapter(): RecyclerView.Adapter<ClubFilterRVAdapter.ClubFilterViewHolder>() {
    interface MyEventListener {
        fun check(clubIdList: ArrayList<Int>)
    }

    private var clubs: ArrayList<ClubFilterEntity> = arrayListOf()
    private var checkedClubIdList: ArrayList<Int> = arrayListOf()

    private lateinit var binding: ItemClubFilterBinding
    private lateinit var myEventListener: MyEventListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClubFilterRVAdapter.ClubFilterViewHolder {
        binding = ItemClubFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClubFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClubFilterRVAdapter.ClubFilterViewHolder, position: Int) {
        holder.clubCb.text = clubs[position].name
        holder.clubCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                holder.checkIv.setImageResource(R.drawable.ic_check_white)
                checkedClubIdList.add(clubs[position].clubId)
            } else {
                holder.checkIv.setImageResource(R.drawable.ic_check_primary)
                checkedClubIdList.remove(clubs[position].clubId)
            }

            myEventListener.check(checkedClubIdList)
        }
    }

    override fun getItemCount(): Int = clubs.size

    inner class ClubFilterViewHolder(itemView: ItemClubFilterBinding): RecyclerView.ViewHolder(itemView.root) {
        val clubCb: CheckBox = itemView.clubCb
        val checkIv: ImageView = itemView.clubCheckIv
    }

    fun setData(clubs: ArrayList<ClubFilterEntity>) {
        this.clubs = clubs
        notifyDataSetChanged()
    }

    fun setMyEventListener(myEventListener: MyEventListener) {
        this.myEventListener = myEventListener
    }
}