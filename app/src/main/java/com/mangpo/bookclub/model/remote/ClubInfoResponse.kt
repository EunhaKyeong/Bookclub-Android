package com.mangpo.bookclub.model.remote

import com.mangpo.bookclub.model.entities.Link

data class ClubInfoResponse(
    val data: ClubInfo
)

data class ClubInfo(
    val totalUser: Int,
    val totalPosts: Int,
    val totalBooks: Int,
    val totalComments: Int,
    val totalLikes: Int,
    val clubResponseDto: ClubDetail,
    val userResponseDtos: ArrayList<UserResponse>,
    val bookAndUserDtos: ArrayList<BookInClub>,
    val trendingPostDtos: ArrayList<TrendingPost>
)

data class ClubDetail(
    val id: Int,
    val name: String,
    val level: Int,
    val presidentId: Int,
    val description: String,
    val lastAddBookDate: String,
    val createdDate: String,
    val modifiedDate: String
)

data class TrendingPost(
    val nickname: String,
    val profileImgLocation: String,
    val bookName: String,
    val postResponseDto: PostDetail
)

data class PostDetail(
    val postId: Int,
    var scope: String,
    val isIncomplete: Boolean,
    var title: String,
    var content: String,
    val createdDate: String,
    val modifiedDate: String,
    var location: String,
    var readTime: String,
    var linkResponseDtos: ArrayList<Link>,
    var postImgLocations: ArrayList<String>,
    var clubIdListForScope: ArrayList<Int>,
    val likedList: ArrayList<Liked>,
    val commentsDto: ArrayList<Comment>
)
