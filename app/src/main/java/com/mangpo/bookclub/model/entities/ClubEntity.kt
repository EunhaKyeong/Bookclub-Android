package com.mangpo.bookclub.model.entities

import com.mangpo.bookclub.utils.ClubViewType

data class ClubEntity(
    var clubId: Int? = null,
    var viewType: Int = ClubViewType.EMPTY,
    var name: String = "",
    var description: String = "",
    var level: Int = 0,
    val pageCnt: Int = 0,
    var lastUpdatedDate: String = "",
    var president: ClubMember? = null,
    val members: List<ClubMember> = arrayListOf()
)

data class ClubShortEntity(
    var clubId: Int,
    var name: String,
    var createDate: String
)

data class CreateClubEntity(
    var name: String,
    var description: String
)

data class ClubMember(
    var memberId: Int? = null,
    val profile: String = ""
)
