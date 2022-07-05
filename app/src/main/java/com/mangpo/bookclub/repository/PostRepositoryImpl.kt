package com.mangpo.bookclub.repository

import com.mangpo.bookclub.model.entities.RecordRequest
import com.mangpo.bookclub.model.entities.RecordUpdateRequest
import com.mangpo.bookclub.model.remote.PostDetail
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.service.PostService
import com.mangpo.bookclub.utils.ImgUtils
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepositoryImpl: PostRepository {
    private val postService: PostService = ApiClient.postService

    override fun createPost(
        record: RecordRequest,
        onResponse: (Response<PostDetail>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        postService.createPost(record).enqueue(object : Callback<PostDetail> {
            override fun onResponse(
                call: Call<PostDetail>,
                response: Response<PostDetail>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun uploadImgFile(
        imgPaths: List<String>,
        onResponse: (Response<List<String>>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val multipartBodyList: ArrayList<MultipartBody.Part> = arrayListOf()
        for (imgPath in imgPaths)
            multipartBodyList.add(ImgUtils.prepareFilePart("data", imgPath))

        postService.uploadImgFile(multipartBodyList).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun deletePost(
        postId: Int,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        postService.deletePost(postId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun deletePhotos(
        deletePhotos: List<String>,
        onResponse: (Response<Void>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        postService.deleteMultipleFiles(deletePhotos).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun updatePost(
        postId: Int,
        updateRecord: RecordUpdateRequest,
        onResponse: (Response<PostDetail>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        postService.updatePost(postId, updateRecord).enqueue(object : Callback<PostDetail> {
            override fun onResponse(call: Call<PostDetail>, response: Response<PostDetail>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}