package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mangpo.bookclub.databinding.FragmentBookListBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.view.adapter.BookAdapter
import com.mangpo.bookclub.view.adapter.OnItemClick
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BookListFragment(private val category: String) : Fragment(), OnItemClick {
    private lateinit var binding: FragmentBookListBinding

    private val bookAdapter: BookAdapter = BookAdapter(this)
    private val bookVm: BookViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BookListFragment", "onCreate")

        bookAdapter.setBooks(bookVm.getBookList(category))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("BookListFragment", "onCreateView")
        binding = FragmentBookListBinding.inflate(inflater, container, false)  //뷰바인딩 초기화

        binding.bookListRecyclerView.adapter = bookAdapter    //어댑터 설정
        binding.bookListRecyclerView.layoutManager = GridLayoutManager(this.context, 3) //레이아웃 설정

        observe()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BookListFragment", "onViewCreated")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("BookListFragment", "onDetach")
    }

    override fun onClick(position: Int) {
        var book: BookModel = bookVm.getBookList(bookVm.readType.value!!)!![position]

        (requireActivity() as MainActivity).moveToBookDesc(book)
    }

    private fun observe() {
        bookVm.readType.observe(viewLifecycleOwner, Observer {
            bookAdapter.setBooks(bookVm.getBookList(it))
        })

        bookVm.myLibrarySearch.observe(viewLifecycleOwner, Observer {
            val search = it

            val books = when (bookVm.readType.value) {
                "NOW" -> bookVm.nowBooks.value
                "AFTER" -> bookVm.afterBooks.value
                else -> bookVm.beforeBooks.value
            }

            bookAdapter.setBooks(books?.filter { it ->
                it.name!!.contains(search)
            } as MutableList<BookModel>)
        })

        bookVm.myLibrarySort.observe(viewLifecycleOwner, Observer { it ->
            val books = when (bookVm.readType.value) {
                "NOW" -> bookVm.nowBooks.value
                "AFTER" -> bookVm.afterBooks.value
                else -> bookVm.beforeBooks.value
            }

            if (books != null && books.isNotEmpty()) {
                when (it) {
                    "Latest" -> bookAdapter.setBooks(
                        books.sortedWith(compareBy { it.modifiedDate })
                            .reversed() as MutableList<BookModel>
                    )
                    "Old" -> bookAdapter.setBooks(books.sortedWith(compareBy { it.modifiedDate }) as MutableList<BookModel>)
                    "Name" -> bookAdapter.setBooks(books.sortedWith(compareBy { it.name }) as MutableList<BookModel>)
                    else -> bookAdapter.setBooks(books)
                }
            }
        })

        bookVm.getBooksCode.observe(viewLifecycleOwner, Observer {
            if (it != 200)
                Toast.makeText(
                    requireContext(),
                    "책 로딩 중 오류가 발생했습니다. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
        })
    }
}