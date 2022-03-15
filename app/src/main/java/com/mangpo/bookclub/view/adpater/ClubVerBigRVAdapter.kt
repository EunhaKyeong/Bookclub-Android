package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemClubVerBigBinding
import com.mangpo.bookclub.utils.convertDpToPx

class ClubVerBigRVAdapter(): RecyclerView.Adapter<ClubVerBigRVAdapter.ClubViewHolder>() {
    interface MyClickListener {
        fun goClubDetailView()
    }

    private lateinit var binding: ItemClubVerBigBinding
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClubVerBigRVAdapter.ClubViewHolder {
        binding = ItemClubVerBigBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClubVerBigRVAdapter.ClubViewHolder, position: Int) {
        val px12 = convertDpToPx(binding.root.context, 12)
        val px20 = convertDpToPx(binding.root.context, 20)
        when (position) {
            0 -> holder.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(px20, 0, px12, 0)
            }
            2 -> holder.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(0, 0, px20, 0)
            }
            else -> holder.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(0, 0, px12, 0)
            }
        }

        holder.root.setOnClickListener {
            myClickListener.goClubDetailView()
        }
    }

    override fun getItemCount(): Int = 3

    inner class ClubViewHolder(itemView: ItemClubVerBigBinding): RecyclerView.ViewHolder(itemView.root) {
        val root: ConstraintLayout = itemView.root
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }
}