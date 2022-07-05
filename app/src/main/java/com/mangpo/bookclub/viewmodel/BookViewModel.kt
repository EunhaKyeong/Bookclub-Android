package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.ApplicationClass.Companion.database
import com.mangpo.bookclub.BuildConfig
import com.mangpo.bookclub.dao.BookDao
import com.mangpo.bookclub.model.entities.BookCategoryRequest
import com.mangpo.bookclub.model.entities.BookEntity
import com.mangpo.bookclub.model.entities.BookRequest
import com.mangpo.bookclub.model.remote.BookInLib
import com.mangpo.bookclub.model.remote.KakaoBook
import com.mangpo.bookclub.model.remote.PostDetail
import com.mangpo.bookclub.repository.BookRepositoryImpl
import com.mangpo.bookclub.utils.LogUtil
import kotlinx.coroutines.*

class BookViewModel: BaseViewModel() {
    private val bookRepository: BookRepositoryImpl = BookRepositoryImpl()
    private val bookDao: BookDao = database.bookDao()

    private val _kakaoBooks: MutableLiveData<List<BookInLib>> = MutableLiveData()
    val kakaoBooks: LiveData<List<BookInLib>> get() = _kakaoBooks

    private val _books: MutableLiveData<List<BookInLib>> = MutableLiveData()
    val books: LiveData<List<BookInLib>> get() = _books

    private val _createBookCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val createBookCode:  LiveData<Event<Int>> get() = _createBookCode
    var newBookInLib: BookInLib? = null

    private val _updateBookCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val updateBookCode: LiveData<Event<Int>> get() = _updateBookCode

    private val _deleteBookCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val deleteBookCode: LiveData<Event<Int>> get() = _deleteBookCode

    private val _nowBooks: MutableLiveData<ArrayList<BookInLib>> = MutableLiveData()
    val nowBooks: LiveData<ArrayList<BookInLib>> get() = _nowBooks

    private val _afterBooks: MutableLiveData<ArrayList<BookInLib>> = MutableLiveData()
    val afterBooks: LiveData<ArrayList<BookInLib>> get() = _afterBooks

    private val _beforeBooks: MutableLiveData<ArrayList<BookInLib>> = MutableLiveData()
    val beforeBooks: LiveData<ArrayList<BookInLib>> get() = _beforeBooks

    private val _records: MutableLiveData<List<PostDetail>> = MutableLiveData()
    val records: LiveData<List<PostDetail>> get() = _records

    private fun kakaoBookToBook(kakaoBook: KakaoBook): BookInLib = BookInLib(
        name = kakaoBook.title,
        isbn = kakaoBook.isbn,
        image = kakaoBook.thumbnail
    )

    private fun setBookImg(position: Int, bookInLibs: List<BookInLib>, category: String) {
        if (position==bookInLibs.size) {
            _books.postValue(bookInLibs)

            when (category) {
                "NOW" -> _nowBooks.postValue((bookInLibs as ArrayList<BookInLib>))
                "AFTER" -> _afterBooks.postValue(bookInLibs as ArrayList<BookInLib>)
                "BEFORE" -> _beforeBooks.postValue(bookInLibs as ArrayList<BookInLib>)
            }

            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val img = bookDao.getImageByIsbn(bookInLibs[position].isbn)

            if (img==null || img.isBlank()) {
                bookRepository.searchBooks(
                    query = bookInLibs[position].isbn,
                    target = "isbn",
                    size = 1,
                    onResponse = {
                        viewModelScope.launch {
                            LogUtil.d("BookViewModel", "searchBookThumbnail Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                            bookInLibs[position].image = it.body()!!.documents[0].thumbnail
                            val job = launch(Dispatchers.IO) {
                                bookDao.insert(BookEntity(isbn = bookInLibs[position].isbn, image = it.body()!!.documents[0].thumbnail))
                            }
                            job.join()
                            setBookImg(position + 1, bookInLibs, category)
                        }
                    },
                    onFailure = {
                        LogUtil.e("BookViewModel", "searchBookThumbnail Fail!\nmessage: ${it.message}")
                    }
                )
            } else {
                bookInLibs[position].image = img
                setBookImg(position + 1, bookInLibs, category)
            }
        }
    }

    fun searchBooks(title: String, target: String, size: Int) {
        bookRepository.searchBooks(
            query = title,
            target = target,
            size = size,
            onResponse = {
                LogUtil.d("BookViewModel", "searchBooks Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==200) {
                    val kakaoBooks = it.body()!!.documents
                    val bookInLibs: ArrayList<BookInLib> = arrayListOf()

                    for (book in kakaoBooks) {
                        bookInLibs.add(kakaoBookToBook(book))
                    }

                    _kakaoBooks.value = bookInLibs
                }
            },
            onFailure = {
                LogUtil.e("BookViewModel", "searchBooks Fail!\nmessage: ${it.message}")
            }
        )
    }

    fun getBooksByCategory(category: String) {
        bookRepository.getBooksByCategory(
            category = category,
            onResponse = {
                LogUtil.d("BookViewModel", "getBooksByCategory Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==200)
                    setBookImg(0, it.body()!!.data, category)
            },
            onFailure = {
                LogUtil.e("BookViewModel", "getBooksByCategory Fail!\nmessage: ${it.message}")
            }
        )
    }

    fun createBook(bookInLib: BookInLib) {
        val isbn: String = bookInLib.isbn.split(" ")[0]
        val bookReq: BookRequest = BookRequest(name = bookInLib.name, isbn = isbn, category = bookInLib.category)

        bookRepository.createBook(
            book = bookReq,
            onResponse = {
                LogUtil.d("BookViewModel", "createBook Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==201) {
                    newBookInLib = it.body()!!
                    viewModelScope.launch (Dispatchers.IO) {
                        bookDao.insert(BookEntity(isbn = isbn, image = bookInLib.image))
                    }

                    when (it.body()!!.category) {
                        "NOW" -> _nowBooks.value?.add(it.body()!!)
                        "AFTER" -> _afterBooks.value?.add(it.body()!!)
                        else -> _beforeBooks.value?.add(it.body()!!)
                    }
                }

                _createBookCode.value = Event(it.code())
            },
            onFailure = {
                LogUtil.e("BookViewModel", "createBook Fail!\nmessage: ${it.message}")
                _createBookCode.value = Event(600)
            }
        )
    }

    fun updateBook(bookId: Int, category: String) {
        bookRepository.updateBook(
            bookId = bookId,
            categoryReq = BookCategoryRequest(category),
            onResponse = {
                LogUtil.d("BookViewModel", "updateBook Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                if (it.code()==204) {
                    when (category) {
                        "NOW" -> _updateBookCode.value = Event(2040)
                        "AFTER" -> _updateBookCode.value = Event(2041)
                        "BEFORE" -> _updateBookCode.value = Event(2042)
                    }
                } else
                    _updateBookCode.value = Event(it.code())

            },
            onFailure = {
                LogUtil.e("BookViewModel", "updateBook Fail!\nmessage: ${it.message}")
                _updateBookCode.value = Event(600)
            }
        )
    }

    fun deleteBook(bookId: Int) {
        bookRepository.deleteBook(
            bookId = bookId,
            onResponse = {
                LogUtil.d("BookViewModel", "deleteBook Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _deleteBookCode.value = Event(it.code())
            },
            onFailure = {
                LogUtil.e("BookViewModel", "updateBook Fail!\nmessage: ${it.message}")
                _deleteBookCode.value = Event(600)
            }
        )
    }

    fun getPostsByBookId(bookId: Int) {
        bookRepository.getPostsByBookId(
            bookId = bookId,
            onResponse = {
                LogUtil.d("BookViewModel", "getPostsByBookId Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _records.value = it.body()?.data
            },
            onFailure = {
                LogUtil.e("BookViewModel", "getPostsByBookId Fail!\nmessage: ${it.message}")
                _records.value = null
            }
        )
    }
}