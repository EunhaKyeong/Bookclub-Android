package com.mangpo.bookclub.model.remote

data class ClubListResponse(
    val data: ArrayList<Club>
)

data class Club(
    val id: Int,
    val name: String,
    val level: Int,
    val presidentId: Int,
    val description: String,
    val lastAddBookDate: String,
    val createdDate: String,
    val modifiedDate: String
)
