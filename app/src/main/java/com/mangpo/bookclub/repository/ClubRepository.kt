package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.CreateClubEntity
import com.mangpo.bookclub.model.remote.ClubInfoResponse
import com.mangpo.bookclub.model.remote.ClubListResponse
import com.mangpo.bookclub.model.remote.CreateClubResponse
import retrofit2.Response

interface ClubRepository {
    fun getClubsByUser(userId: Int, onResponse: (Response<ClubListResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun getClubInfoByClubId(clubId: Int, onResponse: (Response<ClubInfoResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun createClub(club: CreateClubEntity, onResponse: (Response<CreateClubResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun deleteClub(clubId: Int, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
}