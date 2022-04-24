package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.bookclub.model.entities.CreateClubEntity
import com.mangpo.bookclub.model.remote.Club
import com.mangpo.bookclub.model.remote.ClubInfo
import com.mangpo.bookclub.repository.ClubRepositoryImpl
import com.mangpo.bookclub.utils.LogUtil

class ClubViewModel: BaseViewModel() {
    private val repository: ClubRepositoryImpl = ClubRepositoryImpl()

    private val _clubList: MutableLiveData<ArrayList<Club>> = MutableLiveData()
    val clubList: LiveData<ArrayList<Club>> get() = _clubList

    private val _createClubCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val createClubCode: LiveData<Event<Int>> get() = _createClubCode

    private val _deleteClubCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val deleteClubCode: LiveData<Event<Int>> get() = _deleteClubCode

    private val _clubInfo: MutableLiveData<ClubInfo> = MutableLiveData()
    val clubInfo: LiveData<ClubInfo> get() = _clubInfo

    fun getClubsByUser(userId: Int) {
        repository.getClubsByUser(
            userId=userId,
            onResponse = {
                LogUtil.d("ClubViewModel", "getClubsByUser Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _clubList.postValue(it.body()!!.data)
            },
            onFailure = {
                LogUtil.e("ClubViewModel", "getClubsByUser Fail!\nmessage: ${it.message}")
            }
        )
    }

    fun createClub(club: CreateClubEntity) {
        repository.createClub(
            club = club,
            onResponse = {
                LogUtil.d("ClubViewModel", "createClub Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _createClubCode.value = Event(it.code())
            },
            onFailure = {
                LogUtil.e("ClubViewModel", "createClub Fail!\nmessage: ${it.message}")
                _createClubCode.value = Event(600)
            }
        )
    }

    fun deleteClub(clubId: Int) {
        repository.deleteClub(
            clubId = clubId,
            onResponse = {
                LogUtil.d("ClubViewModel", "deleteClub Success!\ncode: ${it.code()}")
                _deleteClubCode.value = Event(it.code())
            },
            onFailure = {
                LogUtil.e("ClubViewModel", "deleteClub Fail!\nmessage: ${it.message}")
                _deleteClubCode.value = Event(600)
            }
        )
    }

    fun getClubInfoByClubId(clubId: Int) {
        repository.getClubInfoByClubId(
            clubId = clubId,
            onResponse = {
                LogUtil.d("ClubViewModel", "getClubInfoByClubId Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _clubInfo.value = it.body()!!.data
            },
            onFailure = {
                LogUtil.e("ClubViewModel", "createClub Fail!\nmessage: ${it.message}")
                _clubInfo.value = null
            }
        )
    }
}