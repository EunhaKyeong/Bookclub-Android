package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.entities.CreateClubEntity
import com.mangpo.bookclub.model.remote.ClubInfoResponse
import com.mangpo.bookclub.model.remote.ClubListResponse
import com.mangpo.bookclub.model.remote.CreateClubResponse
import retrofit2.Call
import retrofit2.http.*

interface ClubService {
    @GET("/clubs")
    fun getClubsByUser(@Query("userId") userId: Int): Call<ClubListResponse>
    @GET("/clubs/{clubId}")
    fun getClubInfoByClubId(@Path("clubId") clubId: Int): Call<ClubInfoResponse>

    @POST("/clubs")
    fun createClub(@Body club: CreateClubEntity): Call<CreateClubResponse>

    @DELETE("/clubs/{clubId}")
    fun deleteClub(@Path("clubId") clubId: Int): Call<Void>
}