package com.mangpo.bookclub.model.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class BookResponse(
    val data: List<BookInLib>
)

interface Book

data class BookInLib(
    var id: Int? = null,
    val name: String = "",
    val isbn: String = "",
    var image: String = "",
    var category: String = "",
    val createdDate: String = "",
    val modifiedDate: String = ""
): Book

@Parcelize
data class BookInClub(
    val userId: Int,
    val userNickname: String,
    val bookId: Int,
    val bookName: String,
    val isbn: String,
    var bookImg: String? = null,
    val category: String,
    val createdDate: String,
    val modifiedDate: String
): Parcelable, Book
