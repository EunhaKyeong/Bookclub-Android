package com.mangpo.bookclub.model.remote

data class ClubInfoResponse(
    val data: ClubInfo
)

data class ClubInfo(
    val id: Int,
    val name: String,
    val level: Int,
    val presidentId: Int,
    val description: String,
    val createdDate: String,
    val modifiedDate: String,
    val totalUser: Int,
    val totalPosts: Int,
    val totalBooks: Int,
    val totalComments: Int,
    val totalLikes: Int,
    val userResponseDtos: ArrayList<UserResponse>,
    val bookAndUserDtos: ArrayList<BookInfo>,
    val trendingPosts: ArrayList<TrendingPost>
)

data class BookInfo(
    val userId: Int,
    val userNickname: String,
    val bookId: Int,
    val bookName: String,
    val isbn: Int,
    val category: String,
    val createdDate: String,
    val modifiedDate: String
)

data class TrendingPost(
    val id: Int
)
