package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemClubVerBigBinding
import com.mangpo.bookclub.databinding.ItemNoClubVerBigBinding
import com.mangpo.bookclub.model.entities.ClubEntity
import com.mangpo.bookclub.config.ViewType
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class ClubVerBigRVAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface MyClickListener {
        fun goClubDetailView(clubId: Int)
    }

    private lateinit var myClickListener: MyClickListener

    private var clubs: ArrayList<ClubEntity> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType== ViewType.CLUB)
            ClubViewHolder(ItemClubVerBigBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else
            EmptyClubViewHolder(ItemNoClubVerBigBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (clubs[position].viewType== ViewType.CLUB) {
            (holder as ClubViewHolder).root.setOnClickListener {
                myClickListener.goClubDetailView(clubs[position].clubId!!)
            }

            if (clubs[position].lastAddBookDate==null) {
                if (isNew(clubs[position].lastUpdatedDate))
                    holder.newIcon.visibility = View.VISIBLE
                else
                    holder.newIcon.visibility = View.INVISIBLE
            } else {
                if (isNew(clubs[position].lastAddBookDate!!))
                    holder.newIcon.visibility = View.VISIBLE
                else
                    holder.newIcon.visibility = View.INVISIBLE
            }

            holder.clubName.text = clubs[position].name
            holder.description.text = "\"${clubs[position].description}\""
            holder.pageCnt.text = clubs[position].pageCnt.toString()

            when (clubs[position].level) {
                1 -> holder.levelCharacter.setImageResource(R.drawable.ic_level1_character)
                2 -> holder.levelCharacter.setImageResource(R.drawable.ic_level2_character)
                3 -> holder.levelCharacter.setImageResource(R.drawable.ic_level3_character)
                4 -> holder.levelCharacter.setImageResource(R.drawable.ic_level4_character)
            }
        }
    }

    override fun getItemCount(): Int = this.clubs.size

    override fun getItemViewType(position: Int): Int = clubs[position].viewType

    inner class ClubViewHolder(itemView: ItemClubVerBigBinding): RecyclerView.ViewHolder(itemView.root) {
        val root: ConstraintLayout = itemView.root
        val clubName: TextView = itemView.clubNameTv
        val description: TextView = itemView.clubDescTv
        val pageCnt: TextView = itemView.clubPageCntTv
        val levelCharacter: ImageView = itemView.clubCharacterIv
        val newIcon: ImageView = itemView.clubNewIv
    }

    inner class EmptyClubViewHolder(itemView: ItemNoClubVerBigBinding): RecyclerView.ViewHolder(itemView.root) {

    }

    private fun isNew(date: String): Boolean {
        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val modifiedDate: LocalDateTime = LocalDateTime.parse(date.substring(0, 19), formatter1)
        val duration = Duration.between(LocalDateTime.parse(modifiedDate.format(formatter2), formatter2), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime())
        val compareTime = duration.seconds / 60 / 60

        return compareTime<=24
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun setClubs(clubs: ArrayList<ClubEntity>) {
        this.clubs = clubs
        notifyDataSetChanged()
    }
}