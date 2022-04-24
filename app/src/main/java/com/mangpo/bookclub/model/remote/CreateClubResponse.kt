package com.mangpo.bookclub.model.remote

data class CreateClubResponse(
    val id: Int,
    val name: String,
    val level: Int,
    val presidentId: Int,
    var description: String,
    var createdDate: String,
    var modifiedDate: String
)
