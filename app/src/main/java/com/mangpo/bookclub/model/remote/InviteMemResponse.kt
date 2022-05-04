package com.mangpo.bookclub.model.remote

/*{
    "inviteId": 633,
    "userEmailToInvite": "10@gmail.com",
    "clubId": 630,
    "message": "망포 클럽 초대!"
}*/
data class InviteMemResponse (
    val inviteId: Int,
    val userEmailToInvite: String,
    val clubId: Int,
    val message: String
)