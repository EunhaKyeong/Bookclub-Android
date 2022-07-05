package com.mangpo.bookclub.service

import com.mangpo.bookclub.BuildConfig
import com.mangpo.bookclub.model.entities.BookCategoryRequest
import com.mangpo.bookclub.model.entities.BookRequest
import com.mangpo.bookclub.model.remote.BookInLib
import com.mangpo.bookclub.model.remote.BookResponse
import com.mangpo.bookclub.model.remote.KakaoBookResponse
import com.mangpo.bookclub.model.remote.RecordsResponse
import retrofit2.Call
import retrofit2.http.*

interface BookService {
    @Headers("Authorization: ${BuildConfig.KAKAO_BOOK_KEY}")
    @GET("book")
    fun searchBooks(@Query("query") query: String, @Query("target") target: String, @Query("size") size: Int): Call<KakaoBookResponse>
    @GET("/books")
    fun getBooksByCategory(@Query("category") category: String): Call<BookResponse>
    @GET("/posts")
    fun getPostsByBookId(@Query("bookId") bookId: Int): Call<RecordsResponse>

    @POST("/books")
    fun createBook(@Body book: BookRequest): Call<BookInLib>

    @PATCH("/books/{bookId}")
    fun updateBook(@Path("bookId") bookId: Int, @Body categoryRequest: BookCategoryRequest): Call<Void>

    @DELETE("/books/{bookId}")
    fun deleteBook(@Path("bookId") bookId: Int): Call<Void>
}