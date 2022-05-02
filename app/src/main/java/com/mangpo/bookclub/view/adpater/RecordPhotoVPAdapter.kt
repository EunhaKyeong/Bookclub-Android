package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.ItemRecordDetailPhotoBinding

class RecordPhotoVPAdapter(): RecyclerView.Adapter<RecordPhotoVPAdapter.RecordPhotoViewHolder>() {
    interface MyClickListener {
        fun goPhotoActivity(photos: List<String>, position: Int)
    }

    private var photos: List<String> = listOf()

    private lateinit var binding: ItemRecordDetailPhotoBinding
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordPhotoVPAdapter.RecordPhotoViewHolder {
        binding = ItemRecordDetailPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecordPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecordPhotoVPAdapter.RecordPhotoViewHolder,
        position: Int
    ) {
        holder.image.clipToOutline = true
        Glide.with(binding.root.context).load(photos[position]).into((holder.image))
        holder.image.setOnClickListener {
            myClickListener.goPhotoActivity(photos, position)
        }
    }

    override fun getItemCount(): Int = photos.size

    inner class RecordPhotoViewHolder(itemView: ItemRecordDetailPhotoBinding): RecyclerView.ViewHolder(itemView.root) {
        val image: ImageView = itemView.photoIv
    }

    fun setData(photos: List<String>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    fun setMyCLickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }
}