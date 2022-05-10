package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.CreateClubEntity
import com.mangpo.bookclub.model.entities.InviteMemRequest
import com.mangpo.bookclub.model.remote.*
import retrofit2.Response

interface ClubRepository {
    fun getClubsByUser(userId: Int, onResponse: (Response<ClubListResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getClubInfoByClubId(clubId: Int, onResponse: (Response<ClubInfoResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getClubUserInfo(clubId: Int, userId: Int, onResponse: (Response<MemberResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getInvites(onResponse: (Response<InviteResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun createClub(club: CreateClubEntity, onResponse: (Response<CreateClubResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun inviteMember(inviteMem: InviteMemRequest, onResponse: (Response<InviteMemResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun deleteClub(clubId: Int, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
}