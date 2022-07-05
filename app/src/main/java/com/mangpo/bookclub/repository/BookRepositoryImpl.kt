package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.BookCategoryRequest
import com.mangpo.bookclub.model.entities.BookRequest
import com.mangpo.bookclub.model.remote.BookInLib
import com.mangpo.bookclub.model.remote.BookResponse
import com.mangpo.bookclub.model.remote.KakaoBookResponse
import com.mangpo.bookclub.model.remote.RecordsResponse
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.BookService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookRepositoryImpl: BookRepository {
    private val bookService: BookService = ApiClient.bookService
    private val kakaoBookService: BookService = ApiClient.kakaoBookService

    override fun searchBooks(
        query: String,
        target: String,
        size: Int,
        onResponse: (Response<KakaoBookResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        kakaoBookService.searchBooks(query, target, size).enqueue(object: Callback<KakaoBookResponse> {
            override fun onResponse(
                call: Call<KakaoBookResponse>,
                response: Response<KakaoBookResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<KakaoBookResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun getBooksByCategory(
        category: String,
        onResponse: (Response<BookResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        bookService.getBooksByCategory(category).enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun createBook(
        book: BookRequest,
        onResponse: (Response<BookInLib>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        bookService.createBook(book).enqueue(object : Callback<BookInLib> {
            override fun onResponse(
                call: Call<BookInLib>,
                response: Response<BookInLib>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<BookInLib>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun updateBook(
        bookId: Int,
        categoryReq: BookCategoryRequest,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        bookService.updateBook(bookId, categoryReq).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun deleteBook(
        bookId: Int,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        bookService.deleteBook(bookId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun getPostsByBookId(
        bookId: Int,
        onResponse: (Response<RecordsResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        bookService.getPostsByBookId(bookId).enqueue(object : Callback<RecordsResponse> {
            override fun onResponse(
                call: Call<RecordsResponse>,
                response: Response<RecordsResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<RecordsResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}