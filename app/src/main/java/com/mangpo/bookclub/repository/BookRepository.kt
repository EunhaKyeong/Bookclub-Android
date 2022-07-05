package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.BookCategoryRequest
import com.mangpo.bookclub.model.entities.BookRequest
import com.mangpo.bookclub.model.remote.BookInLib
import com.mangpo.bookclub.model.remote.BookResponse
import com.mangpo.bookclub.model.remote.KakaoBookResponse
import com.mangpo.bookclub.model.remote.RecordsResponse
import retrofit2.Response

interface BookRepository {
    fun searchBooks(query: String, target: String, size: Int, onResponse: (Response<KakaoBookResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getBooksByCategory(category: String, onResponse: (Response<BookResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun createBook(book: BookRequest, onResponse: (Response<BookInLib>) -> Unit, onFailure: (Throwable) -> Unit)
    fun updateBook(bookId: Int, categoryReq: BookCategoryRequest, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun deleteBook(bookId: Int, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getPostsByBookId(bookId: Int, onResponse: (Response<RecordsResponse>) -> Unit, onFailure: (Throwable) -> Unit)
}