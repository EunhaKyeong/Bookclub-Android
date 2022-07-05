package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.bookclub.model.entities.RecordRequest
import com.mangpo.bookclub.model.entities.RecordUpdateRequest
import com.mangpo.bookclub.model.remote.PostDetail
import com.mangpo.bookclub.repository.PostRepositoryImpl
import com.mangpo.bookclub.utils.LogUtil

class PostViewModel: BaseViewModel() {
    private val postRepository: PostRepositoryImpl = PostRepositoryImpl()

    private val _newRecord: MutableLiveData<PostDetail> = MutableLiveData()
    val newRecord: LiveData<PostDetail> get() = _newRecord

    private val _uploadImgPaths: MutableLiveData<List<String>> = MutableLiveData()
    val uploadImgPaths: LiveData<List<String>> get() = _uploadImgPaths

    private val _deletePostCode: MutableLiveData<Int> = MutableLiveData()
    val deletePostCode: LiveData<Int> get() = _deletePostCode

    private val _deletePhotosCode: MutableLiveData<Int> = MutableLiveData()
    val deletePhotosCode: LiveData<Int> get() = _deletePhotosCode

    private val _updateRecord: MutableLiveData<PostDetail> = MutableLiveData()
    val updateRecord: LiveData<PostDetail> get() = _updateRecord

    fun createRecord(record: RecordRequest) {
        postRepository.createPost(
            record = record,
            onResponse = {
                LogUtil.d("PostViewModel", "createRecord Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _newRecord.value = it.body()
            },
            onFailure = {
                LogUtil.e("PostViewModel", "createRecord Fail!\nmessage: ${it.message}")
                _newRecord.value = null
            }
        )
    }

    fun uploadImgPaths(imgPaths: List<String>) {
        postRepository.uploadImgFile(
            imgPaths = imgPaths,
            onResponse = {
                LogUtil.d("PostViewModel", "uploadImgPaths Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _uploadImgPaths.value = it.body()
            },
            onFailure = {
                LogUtil.e("PostViewModel", "uploadImgPaths Fail!\nmessage: ${it.message}")
                _uploadImgPaths.value = null
            }
        )
    }

    fun deletePost(recordId: Int) {
        postRepository.deletePost(
            postId = recordId,
            onResponse = {
                LogUtil.d("PostViewModel", "deletePost Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _deletePostCode.value = it.code()
            },
            onFailure = {
                LogUtil.e("PostViewModel", "deletePost Fail!\nmessage: ${it.message}")
                _deletePostCode.value = 600
            }
        )
    }

    fun deleteMultiplePhotos(deletePhotos: List<String>) {
        postRepository.deletePhotos(
            deletePhotos = deletePhotos,
            onResponse = {
                LogUtil.d("PostViewModel", "deleteMultiplePhotos Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _deletePhotosCode.value = it.code()
            },
            onFailure = {
                LogUtil.e("PostViewModel", "deleteMultiplePhotos Fail!\nmessage: ${it.message}")
                _deletePhotosCode.value = 600
            }
        )
    }

    fun updatePost(recordId: Int, updateRecord: RecordUpdateRequest) {
        postRepository.updatePost(
            postId = recordId,
            updateRecord = updateRecord,
            onResponse = {
                LogUtil.d("PostViewModel", "updatePost Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _updateRecord.value = it.body()
            },
            onFailure = {
                LogUtil.e("PostViewModel", "updatePost Fail!\nmessage: ${it.message}")
                _updateRecord.value = null
            }
        )
    }
}