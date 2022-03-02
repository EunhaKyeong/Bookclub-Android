package com.mangpo.ourpage.repository

import com.mangpo.ourpage.model.entities.RecordRequest
import com.mangpo.ourpage.model.entities.RecordUpdateRequest
import com.mangpo.ourpage.model.remote.RecordResponse
import retrofit2.Response

interface PostRepository {
    fun createPost(record: RecordRequest, onResponse: (Response<RecordResponse>) -> Unit, onFailure: (Throwable) -> Unit)
    fun uploadImgFile(imgPaths: List<String>, onResponse: (Response<List<String>>) -> Unit, onFailure: (Throwable) -> Unit)
    fun deletePost(postId: Int, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun deletePhotos(deletePhotos: List<String>, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun updatePost(postId: Int, updateRecord: RecordUpdateRequest, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
}