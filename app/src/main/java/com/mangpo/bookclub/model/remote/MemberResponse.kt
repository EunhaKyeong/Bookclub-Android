package com.mangpo.bookclub.model.remote

data class MemberResponse(
    val data: Member
)

data class Member(
    val userResponseDto: UserResponse,
    val totalPosts: Int,
    val totalBooks: Int,
    val totalComments: Int,
    val invitedDate: String
)
