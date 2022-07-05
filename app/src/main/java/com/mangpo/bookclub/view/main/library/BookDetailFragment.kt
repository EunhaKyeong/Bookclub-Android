package com.mangpo.bookclub.view.main.library

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookDetailBinding
import com.mangpo.bookclub.model.domain.Record
import com.mangpo.bookclub.model.domain.RecordDetail
import com.mangpo.bookclub.model.remote.BookInLib
import com.mangpo.bookclub.model.remote.PostDetail
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.PrefsUtils
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.MemoRVAdapter
import com.mangpo.bookclub.view.dialog.ActionDialogFragment
import com.mangpo.bookclub.viewmodel.BookViewModel

class BookDetailFragment : BaseFragment<FragmentBookDetailBinding>(FragmentBookDetailBinding::inflate) {
    private val args: BookDetailFragmentArgs by navArgs()
    private val bookVm: BookViewModel by activityViewModels<BookViewModel>()

    private lateinit var actionDialogFragment: ActionDialogFragment
    private lateinit var memoRVAdapter: MemoRVAdapter
    private lateinit var bookInLib: BookInLib

    override fun initAfterBinding() {
        setMyEventListener()
        initAdapter()
        initActionDialogFragment()
        observe()

        bookInLib = Gson().fromJson(args.book, BookInLib::class.java)
        bookVm.getPostsByBookId(bookInLib.id!!)
        bindBook()
    }

    private fun bindBook() {
        binding.bookDetailBookIv.clipToOutline = true
        Glide.with(requireContext()).load(bookInLib.image).into(binding.bookDetailBookIv)
        binding.bookDetailBookNameTv.text = bookInLib.name
        setBookCategoryUI(bookInLib.category)
    }

    private fun setBookCategoryUI(category: String) {
        when (category) {
            "NOW" -> {
                binding.bookDetailReadingIv.setImageResource(R.drawable.ic_eye_blue)
                binding.bookDetailReadingTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))

                binding.bookDetailReadAfterIv.setImageResource(R.drawable.ic_check_circle_bg_grey)
                binding.bookDetailReadAfterTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))
            }
            "AFTER" -> {
                binding.bookDetailReadingIv.setImageResource(R.drawable.ic_eye_grey)
                binding.bookDetailReadingTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))

                binding.bookDetailReadAfterIv.setImageResource(R.drawable.ic_check_circle_bg_primary)
                binding.bookDetailReadAfterTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            }
            "BEFORE" -> {
                binding.bookDetailReadingIv.setImageResource(R.drawable.ic_eye_grey)
                binding.bookDetailReadingTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))

                binding.bookDetailReadAfterIv.setImageResource(R.drawable.ic_check_circle_bg_grey)
                binding.bookDetailReadAfterTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))
            }
        }
    }

    private fun setMyEventListener() {
        binding.bookDetailBackIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.bookDetailReadingView.setOnClickListener {
            if (!isNetworkAvailable(requireContext()))
                showNetworkSnackBar()
            else if (bookInLib.category=="NOW")
                bookVm.updateBook(bookInLib.id!!, "BEFORE")
            else
                bookVm.updateBook(bookInLib.id!!, "NOW")
        }

        binding.bookDetailReadAfterView.setOnClickListener {
            if (!isNetworkAvailable(requireContext()))
                showNetworkSnackBar()
            else if (bookInLib.category=="AFTER")
                bookVm.updateBook(bookInLib.id!!, "BEFORE")
            else
                bookVm.updateBook(bookInLib.id!!, "AFTER")
        }

        binding.bookDetailDeleteView.setOnClickListener {
            actionDialogFragment.show(requireActivity().supportFragmentManager, null)
        }

        binding.bookDetailRecordView.setOnClickListener {
            PrefsUtils.setTempRecord("")
            val action = BookDetailFragmentDirections.actionBookDetailFragmentToRecordFragment(Record(bookId = bookInLib.id, bookTitle = bookInLib.name))
            findNavController().navigate(action)
        }
    }

    private fun initAdapter() {
        memoRVAdapter = MemoRVAdapter()
        memoRVAdapter.setMyClickListener(object : MemoRVAdapter.MyClickListener {
            override fun sendRecord(record: PostDetail) {
                val action = BookDetailFragmentDirections.actionBookDetailFragmentToRecordDetailFragment(mappingToRecord(record, bookInLib))
                findNavController().navigate(action)
            }
        })
        binding.bookDetailRv.adapter = memoRVAdapter
    }

    private fun mappingToRecord(post: PostDetail, book: BookInLib): RecordDetail = RecordDetail(
        recordId = post.postId,
        date = post.modifiedDate,
        bookName = book.name,
        writer = null,
        scope = post.scope,
        title = post.title,
        content = post.content,
        photos = post.postImgLocations,
        location = post.location,
        readTime = post.readTime,
        hyperlinks = post.linkResponseDtos,
        likes = post.likedList,
        comments = post.commentsDto,
        clubList = post.clubIdListForScope
    )

    private fun initActionDialogFragment() {
        actionDialogFragment = ActionDialogFragment()

        val bundle: Bundle = Bundle()
        bundle.apply {
            putString("title", getString(R.string.msg_delete_book))
            putString("desc", getString(R.string.msg_delete_book_desc))
        }
        actionDialogFragment.arguments = bundle

        actionDialogFragment.setMyDialogCallback(object : ActionDialogFragment.MyDialogCallback {
            override fun action1() {
                showLoadingDialog()
                bookVm.deleteBook(bookInLib.id!!)
            }

            override fun action2() {
            }
        })
    }

    private fun getBooks() {
        bookVm.getBooksByCategory("NOW")
        bookVm.getBooksByCategory("AFTER")
        bookVm.getBooksByCategory("BEFORE")
    }

    private fun observe() {
        bookVm.updateBookCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("BookDetailFragment", "updateBookCode Observe! updateBookCode -> $code")

            if (code!=null) {
                when (code) {
                    2040 -> {
                        bookInLib.category = "NOW"
                        setBookCategoryUI("NOW")
                        getBooks()
                    }
                    2041 -> {
                        bookInLib.category = "AFTER"
                        setBookCategoryUI("AFTER")
                        getBooks()
                    }
                    2042 -> {
                        bookInLib.category = "BEFORE"
                        setBookCategoryUI("BEFORE")
                        getBooks()
                    }
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })

        bookVm.deleteBookCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("BookDetailFragment", "deleteBookCode Observe! deleteBookCode -> $code")

            if (code!=null) {
                dismissLoadingDialog()
                showToast("책이 삭제되었습니다.")

                when (code) {
                    204 -> {
                        getBooks()
                        findNavController().popBackStack()
                    }
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })

        bookVm.records.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookDetailFragment", "records Observe! records -> $it")

            if (it==null)
                showSnackBar(getString(R.string.error_api))
            else
                memoRVAdapter.setDate(it)
        })
    }
}