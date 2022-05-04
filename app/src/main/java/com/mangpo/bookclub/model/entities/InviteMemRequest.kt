package com.mangpo.bookclub.model.entities

/*{
    "userEmailToInvite": "10@gmail.com",
    "clubId": 630,
    "message": "망포 클럽 초대!"
}*/
data class InviteMemRequest(
    val userEmailToInvite: String,
    val clubId: Int,
    val message: String
)
