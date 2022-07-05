package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemBookBinding
import com.mangpo.bookclub.model.remote.Book
import com.mangpo.bookclub.model.remote.BookInClub
import com.mangpo.bookclub.model.remote.BookInLib
import com.mangpo.bookclub.utils.convertDpToPx
import com.mangpo.bookclub.utils.getDeviceWidth

class BookRVAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    interface MyClickListener {
        fun sendBook(bookInLib: BookInLib)
    }

    private lateinit var binding: ItemBookBinding
    private lateinit var myClickListener: MyClickListener
    private lateinit var itemFilter: ItemFilter

    private var books: List<Book> = arrayListOf()
    private var filteredBooks: ArrayList<Book> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        itemFilter = ItemFilter(viewType)

        return when (viewType) {
            LIB -> {
                binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LibViewHolder(binding)
            }
            CLUB -> {
                binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ClubViewHolder(binding)
            }
            else -> throw IllegalStateException("Not Found ViewHolder Type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LibViewHolder -> holder.bind(filteredBooks[position] as BookInLib)
            is ClubViewHolder -> holder.bind(filteredBooks[position] as BookInClub)
        }
    }

    override fun getItemCount(): Int = filteredBooks.size

    override fun getItemViewType(position: Int) = when (filteredBooks[position]) {
        is BookInLib -> LIB
        is BookInClub -> CLUB
        else -> throw IllegalStateException("Not Found ViewHolder Type")
    }

    override fun getFilter(): Filter = itemFilter

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun sortClubBooks(type: String) {
        this.filteredBooks.clear()

        when (type) {
            "new" -> this.filteredBooks.addAll(books.sortedWith(compareBy { (it as BookInClub).modifiedDate }).reversed())
            "old" -> this.filteredBooks.addAll(books.sortedWith(compareBy { (it as BookInClub).modifiedDate }))
            "name" -> this.filteredBooks.addAll(books.sortedWith(compareBy { (it as BookInClub).bookName }))
        }

        this.notifyDataSetChanged()
    }

    fun initData() {
        this.filteredBooks.clear()
        this.filteredBooks.addAll(books)
        notifyDataSetChanged()
    }

    fun setData(books: List<Book>) {
        this.books = books
        filteredBooks.clear()
        filteredBooks.addAll(books)

        notifyDataSetChanged()
    }

    fun clearData() {
        this.books = arrayListOf()
        notifyDataSetChanged()
    }

    inner class LibViewHolder(itemView: ItemBookBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val root: ConstraintLayout = itemView.root
        private val bookImg: ImageView = itemView.bookIv
        private val bookTitle: TextView = itemView.bookTv

        fun bind(item: BookInLib) {
            val width = (getDeviceWidth() - convertDpToPx(root.context, 60)) / 3
            val height = (13 * width) / 9
            val params = bookImg.layoutParams
            params.width = width
            params.height = height
            bookImg.layoutParams = params
            bookImg.clipToOutline = true
            if (item.image.isBlank())
                bookImg.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.default_book))
            else
                Glide.with(binding.root.context).load(item.image).into(bookImg)
            bookImg.setOnClickListener {
                myClickListener.sendBook(item)
            }

            bookTitle.text = item.name
            bookTitle.setOnClickListener {
                myClickListener.sendBook(item)
            }
        }
    }

    inner class ClubViewHolder(itemView: ItemBookBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val root: ConstraintLayout = itemView.root
        private val bookImg: ImageView = itemView.bookIv
        private val bookTitle: TextView = itemView.bookTv

        fun bind(item: BookInClub) {
            val width = (getDeviceWidth() - convertDpToPx(root.context, 60)) / 3
            val height = (13 * width) / 9
            val params = bookImg.layoutParams
            params.width = width
            params.height = height
            bookImg.layoutParams = params
            bookImg.clipToOutline = true
            if (item.bookImg!!.isBlank())
                bookImg.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.default_book))
            else
                Glide.with(binding.root.context).load(item.bookImg).into(bookImg)

            bookTitle.text = item.bookName
        }
    }

    inner class ItemFilter(private val viewType: Int): Filter() {
        override fun performFiltering(filter: CharSequence?): FilterResults {
            val filterString = filter.toString()
            val results = FilterResults()

            val filteredList: ArrayList<Book> = arrayListOf()
            if (filterString.isBlank()) {
                results.values = books
                results.count = books.size

                return results
            } else {
                for (book in books) {
                    when (viewType) {
                        LIB -> {
                            if ((book as BookInLib).name.contains(filterString))
                                filteredList.add(book)
                        }
                        CLUB -> {
                            if ((book as BookInClub).bookName.contains(filterString))
                                filteredList.add(book)
                        }
                    }
                }
            }

            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            filteredBooks.clear()
            filteredBooks.addAll(p1!!.values as ArrayList<Book>)
            notifyDataSetChanged()
        }

    }

    companion object {
        private const val LIB = 0
        private const val CLUB = 1
    }
}