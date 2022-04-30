package com.mangpo.bookclub.model.entities

import android.os.Parcelable
import com.mangpo.bookclub.config.ClubViewType
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

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
    val profile: String = "",
)

data class ClubMemberDetail(
    var memberId: Int? = null,
    val nickname: String,
    val profile: String,
    val introduce: String
)

data class ClubFilterEntity(
    var clubId: Int,
    var name: String
)

@Parcelize
data class ClubInfoEntity(
    val clubId: Int,
    val clubName: String,
    val clubLevel: Int,
    val totalMemberCnt: Int,
    val totalMemoCnt: Int,
    val totalBookCnt: Int,
    val totalCommentCnt: Int,
    val totalLikeCnt: Int,
    val presidentId: Int,
    val members: @RawValue ArrayList<ClubMemberDetail>
): Parcelable
