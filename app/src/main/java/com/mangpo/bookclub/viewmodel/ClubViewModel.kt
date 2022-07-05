package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.ApplicationClass.Companion.database
import com.mangpo.bookclub.dao.BookDao
import com.mangpo.bookclub.model.entities.BookEntity
import com.mangpo.bookclub.model.entities.CreateClubEntity
import com.mangpo.bookclub.model.entities.InviteMemRequest
import com.mangpo.bookclub.model.remote.*
import com.mangpo.bookclub.repository.BookRepositoryImpl
import com.mangpo.bookclub.repository.ClubRepositoryImpl
import com.mangpo.bookclub.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClubViewModel: BaseViewModel() {
    private val clubRepository: ClubRepositoryImpl = ClubRepositoryImpl()
    private val bookRepository: BookRepositoryImpl = BookRepositoryImpl()
    private val bookDao: BookDao = database.bookDao()

    private val _clubList: MutableLiveData<ArrayList<Club>> = MutableLiveData()
    val clubList: LiveData<ArrayList<Club>> get() = _clubList

    private val _createClubCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val createClubCode: LiveData<Event<Int>> get() = _createClubCode

    private val _deleteClubCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val deleteClubCode: LiveData<Event<Int>> get() = _deleteClubCode

    private val _clubInfo: MutableLiveData<ClubInfo> = MutableLiveData()
    val clubInfo: LiveData<ClubInfo> get() = _clubInfo

    private val _memberInfo: MutableLiveData<Member> = MutableLiveData()
    val memberInfo: LiveData<Member> get() = _memberInfo

    private val _inviteResultCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val inviteResultCode: LiveData<Event<Int>> get() = _inviteResultCode

    private val _inviteList: MutableLiveData<List<Invite>> = MutableLiveData()
    val inviteList: LiveData<List<Invite>> get() = _inviteList

    private val _books: MutableLiveData<ArrayList<BookInClub>> = MutableLiveData()
    val books: LiveData<ArrayList<BookInClub>> get() = _books

    private lateinit var newClub: CreateClubResponse

    fun getClubsByUser(userId: Int) {
        clubRepository.getClubsByUser(
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
        clubRepository.createClub(
            club = club,
            onResponse = {
                LogUtil.d("ClubViewModel", "createClub Success!\ncode: ${it.code()}\nbody: ${it.body()}")

                if (it.code()==201)
                    newClub = it.body()!!

                _createClubCode.value = Event(it.code())
            },
            onFailure = {
                LogUtil.e("ClubViewModel", "createClub Fail!\nmessage: ${it.message}")
                _createClubCode.value = Event(600)
            }
        )
    }

    fun deleteClub(clubId: Int) {
        clubRepository.deleteClub(
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
        clubRepository.getClubInfoByClubId(
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

    fun setBookImg(books: ArrayList<BookInClub>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (book in books) {
                val img = bookDao.getImageByIsbn(book.isbn)

                if (img==null) {
                    launch(Dispatchers.IO) {
                        bookRepository.searchBooks(
                            query = book.isbn,
                            target = "isbn",
                            size = 1,
                            onResponse = {
                                launch {
                                    LogUtil.d("ClubViewModel", "searchBookThumbnail Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                                    book.bookImg = it.body()!!.documents[0].thumbnail
                                    val job = launch(Dispatchers.IO) {
                                        bookDao.insert(BookEntity(isbn = book.isbn, image = it.body()!!.documents[0].thumbnail))
                                    }
                                    job.join()
                                }
                            },
                            onFailure = {
                                LogUtil.e("ClubViewModel", "searchBookThumbnail Fail!\nmessage: ${it.message}")
                            }
                        )
                    }
                } else {
                    book.bookImg = img
                }
            }

            LogUtil.d("ClubViewModel", "ClubViewModel")
            _books.postValue(books)
        }
    }

    fun getClubUserInfo(clubId: Int, userId: Int) {
        clubRepository.getClubUserInfo(
            clubId = clubId,
            userId = userId,
            onResponse = {
                LogUtil.d("ClubViewModel", "getClubUserInfo Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _memberInfo.value = it.body()!!.data
            },
            onFailure = {
                LogUtil.e("ClubViewModel", "getClubUserInfo Fail!\nmessage: ${it.message}")
                _memberInfo.value = null
            }
        )
    }

    fun inviteMember(member: InviteMemRequest) {
        clubRepository.inviteMember(
            inviteMem = member,
            onResponse = {
                LogUtil.d("ClubViewModel", "inviteMember Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _inviteResultCode.postValue(Event(it.code()))
            },
            onFailure = {
                LogUtil.e("ClubViewModel", "inviteMember Fail!\nmessage: ${it.message}")
                _inviteResultCode.postValue(Event(600))
            }
        )
    }

    fun getInviteList() {
        clubRepository.getInvites(
            onResponse = {
                LogUtil.d("ClubViewModel", "getInviteList Success!\ncode: ${it.code()}\nbody: ${it.body()}")
                _inviteList.postValue(it.body()!!.data)
            },
            onFailure = {
                LogUtil.e("ClubViewModel", "getInviteList Fail!\nmessage: ${it.message}")
            }
        )
    }

    fun getNewClubId(): Int = newClub.id
}