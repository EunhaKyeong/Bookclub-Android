package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.RecordRequest
import com.mangpo.bookclub.model.entities.RecordUpdateRequest
import com.mangpo.bookclub.model.remote.PostDetail
import retrofit2.Response

interface PostRepository {
    fun createPost(record: RecordRequest, onResponse: (Response<PostDetail>) -> Unit, onFailure: (Throwable) -> Unit)
    fun uploadImgFile(imgPaths: List<String>, onResponse: (Response<List<String>>) -> Unit, onFailure: (Throwable) -> Unit)
    fun deletePost(postId: Int, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun deletePhotos(deletePhotos: List<String>, onResponse: (Response<Void>) -> Unit, onFailure: (Throwable) -> Unit)
    fun updatePost(postId: Int, updateRecord: RecordUpdateRequest, onResponse: (Response<PostDetail>) -> Unit, onFailure: (Throwable) -> Unit)
}