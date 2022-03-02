package com.mangpo.ourpage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.ourpage.model.entities.User
import com.mangpo.ourpage.model.remote.UserResponse
import com.mangpo.ourpage.repository.UserRepositoryImpl

class UserViewModel: BaseViewModel() {
    private val userRepositoryImpl: UserRepositoryImpl = UserRepositoryImpl()

    private val _getUserCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val getUserCode: LiveData<Event<Int>> get() = _getUserCode
    private var user: UserResponse? = null

    private val _updateUserCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val updateUserCode: LiveData<Event<Int>> get() = _updateUserCode

    private val _uploadImgFileCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val uploadImgFileCode: LiveData<Event<Int>> get() = _uploadImgFileCode
    private var imgPath: String? = null

    private val _totalMemoCnt: MutableLiveData<Int> = MutableLiveData()
    val totalMemoCnt: LiveData<Int> get() = _totalMemoCnt

    private val _totalBookCnt: MutableLiveData<Int> = MutableLiveData()
    val totalBookCnt: LiveData<Int> get() = _totalBookCnt

    fun getUserInfo() {
        userRepositoryImpl.getCurrentUserInfo(
            onResponse = {
                Log.d("UserViewModel", "getUser Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                user = if (it.code()==200)
                    it.body()!!.data
                else
                    null

                _getUserCode.value = Event(it.code())
            },
            onFailure = {
                Log.e("UserViewModel", "getUser Fail!\nmessage: ${it.message}")
                user = null
            }
        )
    }

    fun getUser(): UserResponse? = user

    fun updateUser(user: User, userId: Int) {
        userRepositoryImpl.updateUser(
            user = user,
            userId = userId,
            onResponse = {
                Log.d("UserViewModel", "updateUser Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _updateUserCode.value = Event(it.code())
            },
            onFailure = {
                Log.e("UserViewModel", "updateUser Fail!\nmessage: ${it.message}")
                _updateUserCode.value = Event(600)
            }
        )
    }

    fun uploadImgFile(imgPath: String) {
        userRepositoryImpl.uploadMultiImgFile(
            imgPaths = listOf(imgPath),
            onResponse = {
                Log.d("UserViewModel", "uploadImgFile Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==200)
                    this.imgPath = it.body()!![0]

                _uploadImgFileCode.value = Event(it.code())
            },
            onFailure = {
                Log.e("UserViewModel", "uploadImgFile Fail!\nmessage: ${it.message}")
                _uploadImgFileCode.value = Event(600)
            }
        )
    }

    fun getImgPath(): String? = imgPath

    fun getTotalMemoCnt() {
        userRepositoryImpl.getTotalMemoCnt(
            onResponse = {
                Log.d("UserViewModel", "getTotalCnt Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==200)
                    _totalMemoCnt.value = it.body()!!.data
                else
                    _totalMemoCnt.value = -1
            },
            onFailure = {
                Log.e("UserViewModel", "getTotalCnt Fail!\nmessage: ${it.message}")
                _totalMemoCnt.value = -1
            }
        )
    }

    fun getTotalBookCnt() {
        userRepositoryImpl.getTotalBookCnt(
            onResponse = {
                Log.d("UserViewModel", "getTotalBookCnt Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==200)
                    _totalBookCnt.value = it.body()!!.data
                else
                    _totalBookCnt.value = -1
            },
            onFailure = {
                Log.e("UserViewModel", "getTotalBookCnt Fail!\nmessage: ${it.message}")
                _totalBookCnt.value = -1
            }
        )
    }
}