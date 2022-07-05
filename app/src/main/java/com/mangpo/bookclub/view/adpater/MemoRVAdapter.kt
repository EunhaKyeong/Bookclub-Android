package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemMemoBinding
import com.mangpo.bookclub.model.remote.PostDetail
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MemoRVAdapter(): RecyclerView.Adapter<MemoRVAdapter.MemoViewHolder>() {
    interface MyClickListener {
        fun sendRecord(record: PostDetail)
    }

    private var records: List<PostDetail> = listOf()

    private lateinit var binding: ItemMemoBinding
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoRVAdapter.MemoViewHolder {
        binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoRVAdapter.MemoViewHolder, position: Int) {
        holder.title.text = records[position].title
        holder.content.text = records[position].content

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val date: LocalDateTime = LocalDateTime.parse(records[position].modifiedDate.substring(0, 19), formatter1)
        holder.date.text = date.format(formatter2)

        holder.root.setOnClickListener {
            myClickListener.sendRecord(records[position])
        }
    }

    override fun getItemCount(): Int = records.size

    fun setDate(records: List<PostDetail>) {
        this.records = records
        notifyDataSetChanged()
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    inner class MemoViewHolder(itemView: ItemMemoBinding): RecyclerView.ViewHolder(itemView.root) {
        val root: ConstraintLayout = itemView.root
        val title: TextView = itemView.memoTitleTv
        val content: TextView = itemView.memoContentTv
        val date: TextView = itemView.memoDateTv
    }
}