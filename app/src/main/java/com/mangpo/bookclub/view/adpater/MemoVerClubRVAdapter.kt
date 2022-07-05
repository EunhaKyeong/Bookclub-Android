package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemMemoVerClubBinding
import com.mangpo.bookclub.model.remote.TrendingPost

class MemoVerClubRVAdapter(): RecyclerView.Adapter<MemoVerClubRVAdapter.MemoVerClubViewHolder>() {
    interface MyClickListener {
        fun goPostDetail(post: TrendingPost)
    }

    private var memos: ArrayList<TrendingPost> = arrayListOf()

    private lateinit var binding: ItemMemoVerClubBinding
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoVerClubRVAdapter.MemoVerClubViewHolder {
        binding = ItemMemoVerClubBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MemoVerClubViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MemoVerClubRVAdapter.MemoVerClubViewHolder,
        position: Int
    ) {
        holder.root.setOnClickListener {
            myClickListener.goPostDetail(memos[position])
        }

        if (memos[position].profileImgLocation==null)
            holder.profile.setImageDrawable(ContextCompat.getDrawable(holder.root.context, R.drawable.bg_default_profile))
        else
            Glide.with(holder.itemView).load(memos[position].profileImgLocation).circleCrop().into(holder.profile)

        holder.memoTitle.text = memos[position].postResponseDto.title
        holder.memoContent.text = memos[position].postResponseDto.content
        holder.bookName.text = memos[position].bookName
        holder.likeCnt.text = memos[position].postResponseDto.likedList.size.toString()
        holder.commentCnt.text = memos[position].postResponseDto.commentsDto.size.toString()
    }

    override fun getItemCount(): Int = memos.size

    inner class MemoVerClubViewHolder(itemView: ItemMemoVerClubBinding): RecyclerView.ViewHolder(itemView.root) {
        val root: ConstraintLayout = itemView.root
        val profile: ImageView = itemView.userIv
        val nickname: TextView = itemView.nicknameTv
        val memoTitle: TextView = itemView.memoTitleTv
        val memoContent: TextView = itemView.memoContentTv
        val bookName: TextView = itemView.bookInfoTv
        val likeCnt: TextView = itemView.likeTv
        val commentCnt: TextView = itemView.commentTv
    }

    fun setData(memos: ArrayList<TrendingPost>) {
        this.memos = memos
        notifyDataSetChanged()
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }
}