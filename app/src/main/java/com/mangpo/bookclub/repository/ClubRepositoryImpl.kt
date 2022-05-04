package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.CreateClubEntity
import com.mangpo.bookclub.model.entities.InviteMemRequest
import com.mangpo.bookclub.model.remote.*
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.ClubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClubRepositoryImpl: ClubRepository {
    private val clubService: ClubService = ApiClient.clubService

    override fun getClubsByUser(
        userId: Int,
        onResponse: (Response<ClubListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        clubService.getClubsByUser(userId).enqueue(object : Callback<ClubListResponse> {
            override fun onResponse(
                call: Call<ClubListResponse>,
                response: Response<ClubListResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<ClubListResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun getClubInfoByClubId(
        clubId: Int,
        onResponse: (Response<ClubInfoResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        clubService.getClubInfoByClubId(clubId).enqueue(object : Callback<ClubInfoResponse> {
            override fun onResponse(
                call: Call<ClubInfoResponse>,
                response: Response<ClubInfoResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<ClubInfoResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun getClubUserInfo(
        clubId: Int,
        userId: Int,
        onResponse: (Response<MemberResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        clubService.getClubUserInfo(clubId, userId).enqueue(object : Callback<MemberResponse> {
            override fun onResponse(
                call: Call<MemberResponse>,
                response: Response<MemberResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun createClub(
        club: CreateClubEntity,
        onResponse: (Response<CreateClubResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        clubService.createClub(club).enqueue(object : Callback<CreateClubResponse> {
            override fun onResponse(
                call: Call<CreateClubResponse>,
                response: Response<CreateClubResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<CreateClubResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun inviteMember(
        inviteMem: InviteMemRequest,
        onResponse: (Response<InviteMemResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        clubService.inviteMember(inviteMem).enqueue(object : Callback<InviteMemResponse> {
            override fun onResponse(
                call: Call<InviteMemResponse>,
                response: Response<InviteMemResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<InviteMemResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun deleteClub(
        clubId: Int,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        clubService.deleteClub(clubId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}