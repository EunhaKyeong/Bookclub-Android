package com.mangpo.bookclub.model.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class UserDataResponse (
    val data: UserResponse
)

@Parcelize
data class UserResponse (
    val userId: Int,
    val email: String,
    var sex: String,
    var birthdate: String,
    var nickname: String,
    var introduce: String?,
    var style: String?,
    var goal: String?,
    var profileImgLocation: String?,
    var genres: List<String>,
    val isDormant: Boolean
) : Parcelable

data class TotalCntResponse (
    val data: Int
)