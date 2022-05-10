package com.mangpo.bookclub.model.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class InviteResponse(
    val data: List<Invite>
)

/*"inviteId": 633,
"userEmailToInvite": "10@gmail.com",
"clubId": 630,
"message": "망포 클럽 초대!"*/

@Parcelize
data class Invite(
    val inviteId: Int,
    val userEmailToInvite: String,
    val clubId: Int,
    val message: String
): Parcelable
