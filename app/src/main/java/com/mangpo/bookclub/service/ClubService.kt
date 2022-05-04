package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.entities.CreateClubEntity
import com.mangpo.bookclub.model.entities.InviteMemRequest
import com.mangpo.bookclub.model.remote.*
import retrofit2.Call
import retrofit2.http.*

interface ClubService {
    @GET("/clubs")
    fun getClubsByUser(@Query("userId") userId: Int): Call<ClubListResponse>
    @GET("/clubs/{clubId}")
    fun getClubInfoByClubId(@Path("clubId") clubId: Int): Call<ClubInfoResponse>
    @GET("/clubs/{clubId}/users/{userId}/info")
    fun getClubUserInfo(@Path("clubId") clubId: Int, @Path("userId") userId: Int): Call<MemberResponse>

    @POST("/clubs")
    fun createClub(@Body club: CreateClubEntity): Call<CreateClubResponse>
    @POST("/invites")
    fun inviteMember(@Body inviteMember: InviteMemRequest): Call<InviteMemResponse>

    @DELETE("/clubs/{clubId}")
    fun deleteClub(@Path("clubId") clubId: Int): Call<Void>
}