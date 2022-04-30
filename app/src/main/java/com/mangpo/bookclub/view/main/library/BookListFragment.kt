package com.mangpo.bookclub.view.main.library

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.config.GlobalVariable
import com.mangpo.bookclub.databinding.FragmentBookListBinding
import com.mangpo.bookclub.model.entities.ClubFilterEntity
import com.mangpo.bookclub.model.remote.Book
import com.mangpo.bookclub.model.remote.Club
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.convertDpToPx
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.BookRVAdapter
import com.mangpo.bookclub.view.adpater.ClubFilterRVAdapter
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.ClubViewModel

class BookListFragment(private val category: String) : BaseFragment<FragmentBookListBinding>(FragmentBookListBinding::inflate), TextWatcher {
    private val bookVm: BookViewModel by activityViewModels<BookViewModel>()
    private val clubVm: ClubViewModel by viewModels()

    private lateinit var bookRVAdapter: BookRVAdapter
    private lateinit var clubFilterRVAdapter: ClubFilterRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        setMyEventListener()
        observe()

        if (category=="BEFORE")
            binding.bookListClubCb.visibility = View.GONE
        else
            binding.bookListClubCb.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        bookVm.getBooksByCategory(category)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0!=null && p0.isNotEmpty()) {
            val books = ((bookVm.books.value) as ArrayList<Book>).filter { it.name.contains(p0) }
            bookRVAdapter.setData(books)
        } else
            setInitBook()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun initAdapter() {
        bookRVAdapter = BookRVAdapter()
        bookRVAdapter.setMyClickListener(object : BookRVAdapter.MyClickListener {
            override fun sendBook(book: Book) {
                val action = LibraryFragmentDirections.actionLibraryFragmentToBookDetailFragment(Gson().toJson(book))
                findNavController().navigate(action)
            }
        })
        binding.bookListRv.adapter = bookRVAdapter

        clubFilterRVAdapter = ClubFilterRVAdapter()
        clubFilterRVAdapter.setMyEventListener(object: ClubFilterRVAdapter.MyEventListener {
            override fun check(clubIdList: ArrayList<Int>) {
                LogUtil.d("BookListFragment", "clubIdList: $clubIdList")

                if (clubIdList.isEmpty())
                    bookVm.getBooksByCategory(category)
            }
        })
        binding.bookListClubFilterRv.adapter = clubFilterRVAdapter
    }

    private fun setMyEventListener() {
        binding.bookListSearchCb.setOnCheckedChangeListener { compoundButton, b ->
            hideKeyboard()
            binding.bookListSearchEt.setText("")
            setInitBook()

            if (b) {
                binding.bookListSearchIv.setImageResource(R.drawable.ic_reading_glasses_white)
                binding.bookListSortCb.isChecked = false
                binding.bookListClubCb.isChecked = false
                binding.bookListSearchEt.visibility = View.VISIBLE
                setRVMargin(71)
            } else {
                binding.bookListSearchIv.setImageResource(R.drawable.ic_reading_glasses_primary)
                binding.bookListSearchEt.visibility = View.GONE
                setRVMargin(41)
            }
        }

        binding.bookListClubCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                clubVm.getClubsByUser(GlobalVariable.userId)

                binding.bookListSearchCb.isChecked = false
                binding.bookListSortCb.isChecked = false

                binding.bookListClubFilterRv.visibility = View.VISIBLE
                setRVMargin(71)
            } else {
                binding.bookListClubFilterRv.visibility = View.GONE
                setRVMargin(41)
            }
        }

        binding.bookListSortCb.setOnCheckedChangeListener { compoundButton, b ->
            binding.bookListNewestCb.isChecked = false
            binding.bookListOldestCb.isChecked = false
            binding.bookListByNameCb.isChecked = false
            setInitBook()

            if (b) {
                binding.bookListSearchCb.isChecked = false
                binding.bookListClubCb.isChecked = false
                binding.bookListNewestCb.visibility = View.VISIBLE
                binding.bookListOldestCb.visibility = View.VISIBLE
                binding.bookListByNameCb.visibility = View.VISIBLE
                setRVMargin(71)
            } else {
                binding.bookListNewestCb.visibility = View.GONE
                binding.bookListOldestCb.visibility = View.GONE
                binding.bookListByNameCb.visibility = View.GONE
                setRVMargin(41)
            }
        }

        binding.bookListSearchEt.addTextChangedListener(this)

        binding.bookListNewestCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.bookListOldestCb.isChecked = false
                binding.bookListByNameCb.isChecked = false
            }

            setBookBySort()
        }

        binding.bookListOldestCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.bookListNewestCb.isChecked = false
                binding.bookListByNameCb.isChecked = false
            }

            setBookBySort()
        }

        binding.bookListByNameCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.bookListNewestCb.isChecked = false
                binding.bookListOldestCb.isChecked = false
            }

            setBookBySort()
        }
    }

    private fun setRVMargin(margin: Int) {
        val horizontalMargin = convertDpToPx(requireContext(), 10)
        val topMargin = convertDpToPx(requireContext(), margin)

        binding.bookListRv.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(horizontalMargin, topMargin, horizontalMargin, 0)
        }
    }

    private fun setInitBook() {
        bookRVAdapter.setData(bookVm.books.value!!)
    }

    private fun setBookBySort() {
        val books = bookVm.books.value as ArrayList<Book>

        when {
            binding.bookListNewestCb.isChecked -> bookRVAdapter.setData(books.sortedWith(compareBy { it.modifiedDate }).reversed())
            binding.bookListOldestCb.isChecked -> bookRVAdapter.setData(books.sortedWith(compareBy { it.modifiedDate }))
            binding.bookListByNameCb.isChecked -> bookRVAdapter.setData(books.sortedWith(compareBy { it.name }))
            else -> setInitBook()
        }
    }

    private fun setLibraryView(books: ArrayList<Book>) {
        if (books.size==0) {
            binding.bookListRv.visibility = View.INVISIBLE
            binding.bookListEmptyIv.visibility = View.VISIBLE
            binding.bookListSadCharacterIv.visibility = View.VISIBLE
            Glide.with(this).load(R.raw.sad_character).into(binding.bookListSadCharacterIv)
        } else {
            binding.bookListRv.visibility = View.VISIBLE
            binding.bookListEmptyIv.visibility = View.INVISIBLE
            binding.bookListSadCharacterIv.visibility = View.INVISIBLE
            bookRVAdapter.setData(books)
        }
    }

    private fun mappingToClubFilterEntity(clubs: ArrayList<Club>): ArrayList<ClubFilterEntity> {
        val clubFilters: ArrayList<ClubFilterEntity> = arrayListOf()
        for (club in clubs) {
            clubFilters.add(ClubFilterEntity(club.id, club.name))
        }

        return clubFilters
    }

    private fun observe() {
        bookVm.nowBooks.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookListFragment", "nowBooks Observe! nowBooks -> $it")

            if (category=="NOW") {
                binding.bookListBookCntTv.text = it.size.toString()
                setLibraryView(it)
            }
        })

        bookVm.afterBooks.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookListFragment", "afterBooks Observe! afterBooks -> $it")

            if (category=="AFTER") {
                binding.bookListBookCntTv.text = it.size.toString()
                setLibraryView(it)
            }
        })

        bookVm.beforeBooks.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookListFragment", "beforeBooks Observe! beforeBooks -> $it")

            if (category=="BEFORE") {
                binding.bookListBookCntTv.text = it.size.toString()
                setLibraryView(it)
            }
        })

        clubVm.clubList.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookListFragment", "clubList Observe! clubList -> $it")

            if (it.isNotEmpty())
                clubFilterRVAdapter.setData(mappingToClubFilterEntity(it))
        })
    }
}