package com.mangpo.bookclub.model.remote

data class RecordsResponse (
    val data: List<PostDetail>
)

data class Liked(
    val userNickname: String,
    val isLiked: Boolean
)

data class Comment(
    val commentId: Int,
    val parentCommentId: Int,
    val userNickname: String,
    val content: String,
    val createDate: String,
    val modifiedDate: String
)