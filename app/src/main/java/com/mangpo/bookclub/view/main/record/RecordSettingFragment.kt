package com.mangpo.bookclub.view.main.record

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.config.GlobalVariable
import com.mangpo.bookclub.databinding.FragmentRecordSettingBinding
import com.mangpo.bookclub.model.domain.RecordDetail
import com.mangpo.bookclub.model.entities.*
import com.mangpo.bookclub.model.remote.BookInLib
import com.mangpo.bookclub.model.remote.Club
import com.mangpo.bookclub.model.remote.PostDetail
import com.mangpo.bookclub.utils.ImgUtils.getAbsolutePathByBitmap
import com.mangpo.bookclub.utils.ImgUtils.uriToBitmap
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.ClubCBRVAdapter
import com.mangpo.bookclub.view.adpater.LinkRVAdapter
import com.mangpo.bookclub.view.dialog.ActionDialogFragment
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.ClubViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel

class RecordSettingFragment : BaseFragment<FragmentRecordSettingBinding>(FragmentRecordSettingBinding::inflate) {
    private val bookVm: BookViewModel by viewModels<BookViewModel>()
    private val postVm: PostViewModel by viewModels<PostViewModel>()
    private val clubVm: ClubViewModel by viewModels<ClubViewModel>()
    private val args: RecordSettingFragmentArgs by navArgs()

    private var clickedLinkPosition: Int? = null

    private lateinit var bookInLib: BookInLib
    private lateinit var recordVerCreate: RecordRequest
    private lateinit var recordVerUpdate: PostDetail
    private lateinit var linkRVAdapter: LinkRVAdapter
    private lateinit var actionDialog: ActionDialogFragment
    private lateinit var clubCbRVAdapter: ClubCBRVAdapter

    override fun initAfterBinding() {
        initActionDialog()
        initAdapter()

        clubVm.getClubsByUser(GlobalVariable.userId)

        if (args.mode=="CREATE" && ::recordVerCreate.isInitialized)
            bindRecordVerCreate()
        else if (args.mode=="CREATE" && !::recordVerCreate.isInitialized)
            recordVerCreate = Gson().fromJson(args.record, RecordRequest::class.java)
        else if (args.mode=="UPDATE" && ::recordVerUpdate.isInitialized)
            bindRecordVerUpdate()
        else {
            recordVerUpdate = Gson().fromJson(args.record, PostDetail::class.java)
            bindRecordVerUpdate()
        }
        bookInLib = Gson().fromJson(args.book, BookInLib::class.java)

        setMyEventListener()
        observe()
    }

    override fun onStop() {
        super.onStop()

        if (args.mode=="CREATE")
            recordVerCreate = setRecordVerCreate()
        else
            recordVerUpdate = setRecordVerUpdate()
    }

    private fun initActionDialog() {
        actionDialog = ActionDialogFragment()
        actionDialog.setMyDialogCallback(object : ActionDialogFragment.MyDialogCallback {
            override fun action1() {
                linkRVAdapter.removeLink(clickedLinkPosition!!)

                if (binding.recordSettingLinkPlusView.visibility==View.GONE) {
                    binding.recordSettingLinkPlusView.visibility = View.VISIBLE
                    binding.recordSettingLinkPlusIv.visibility = View.VISIBLE
                }
            }

            override fun action2() {
            }
        })
    }

    private fun initAdapter() {
        linkRVAdapter = LinkRVAdapter()
        linkRVAdapter.setMyClickListener(object : LinkRVAdapter.MyClickListener {
            override fun removeLink(position: Int) {
                clickedLinkPosition = position
                val bundle: Bundle = Bundle()
                bundle.putString("title", getString(R.string.msg_delete_link))
                bundle.putString("desc", getString(R.string.msg_no_restore_after_delete))

                actionDialog.arguments = bundle
                actionDialog.show(requireActivity().supportFragmentManager, null)
            }
        })

        binding.recordSettingLinkRv.adapter = linkRVAdapter

        clubCbRVAdapter = ClubCBRVAdapter()
        binding.recordSettingClubRv.adapter = clubCbRVAdapter
    }

    private fun setMyEventListener() {
        binding.recordSettingTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.recordSettingCompleteTv.setOnClickListener {
            hideKeyboard()
            showLoadingDialog()

            if (!validateLink()) {    //1. 링크 유효성 검사
                dismissLoadingDialog()
                showToast(getString(R.string.msg_null_link))
            } else if (!isNetworkAvailable(requireContext())) {  //2. 네트워크 상태 확인
                showNetworkSnackBar()
                dismissLoadingDialog()
            } else if (args.mode=="CREATE") {   //3. 기록 추가일 때
                when {
                    bookInLib.id==null -> bookVm.createBook(bookInLib) //3-1. book.id 가 null -> 책 등록
                    recordVerCreate.postImgLocations.isEmpty() -> postVm.createRecord(setRecordVerCreate()) //3-2. 이미지 없는 기록 -> createRecord
                    else -> uploadPhotos()    //3-3. 이미지 있는 기록 -> uploadPost
                }
            } else {    //4. 기록 수정일 때
                if (recordVerUpdate.postImgLocations.isEmpty()) //4-1. 이미지가 없을 때
                    postVm.updatePost(recordVerUpdate.postId, setRecordUpdateRequest())
                else    //4-2. 이미지가 있을 때
                    uploadPhotosVerUpdate()
            }
        }

        binding.recordSettingLinkPlusView.setOnClickListener {
            hideKeyboard()

            if (linkRVAdapter.itemCount==2) {
                it.visibility = View.GONE
                binding.recordSettingLinkPlusIv.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                binding.recordSettingLinkPlusIv.visibility = View.VISIBLE

                if (linkRVAdapter.itemCount==0)
                    binding.recordSettingLinkRv.visibility = View.VISIBLE
            }

            linkRVAdapter.addLink()
        }
    }

    private fun bindRecordVerCreate() {
        binding.recordSettingLocationEt.setText(recordVerCreate.location)
        binding.recordSettingTimeEt.setText(recordVerCreate.readTime)
        setLinkUI(recordVerCreate.linkRequestDtos as ArrayList<Link>)
        clubCbRVAdapter.setCheckedUI(recordVerCreate.clubIdListForScope)
    }

    private fun bindRecordVerUpdate() {
        binding.recordSettingLocationEt.setText(recordVerUpdate.location)
        binding.recordSettingTimeEt.setText(recordVerUpdate.readTime)
        setLinkUI(recordVerUpdate.linkResponseDtos as ArrayList<Link>)
        clubCbRVAdapter.setCheckedUI(recordVerUpdate.clubIdListForScope)
    }

    private fun setLinkUI(links: ArrayList<Link>) {
        if (links.isEmpty()) {
            binding.recordSettingLinkRv.visibility = View.GONE
            binding.recordSettingLinkPlusView.visibility = View.VISIBLE
            binding.recordSettingLinkPlusIv.visibility = View.VISIBLE
        } else {
            linkRVAdapter.setLinks(links)
            binding.recordSettingLinkRv.visibility = View.VISIBLE

            if (links.size==3) {
                binding.recordSettingLinkPlusView.visibility = View.GONE
                binding.recordSettingLinkPlusIv.visibility = View.GONE
            } else {
                binding.recordSettingLinkPlusView.visibility = View.VISIBLE
                binding.recordSettingLinkPlusIv.visibility = View.VISIBLE
            }
        }
    }

    private fun validateLink(): Boolean {
        val links = linkRVAdapter.getLinks()

        if (links.isEmpty()) {
            if (args.mode=="CREATE")
                recordVerCreate.linkRequestDtos = arrayListOf()
            else
                recordVerUpdate.linkResponseDtos = arrayListOf()

            return true
        } else {
            for (link in links) {
                if (link.hyperlinkTitle.isBlank() || link.hyperlink.isBlank())
                    return false
            }

            if (args.mode=="CREATE")
                recordVerCreate.linkRequestDtos = links
            else
                recordVerUpdate.linkResponseDtos = ArrayList(links)
        }

        return true
    }

    private fun uploadPhotos() {
        val absolutePaths: ArrayList<String> = arrayListOf()
        for (img in recordVerCreate.postImgLocations) {
            absolutePaths.add(getAbsolutePathByBitmap(requireContext(), uriToBitmap(requireContext(), Uri.parse(img))))
        }
        postVm.uploadImgPaths(absolutePaths)
    }

    private fun uploadPhotosVerUpdate() {
        val absolutePaths: ArrayList<String> = arrayListOf()
        for (img in recordVerUpdate.postImgLocations) {
            if (!img.startsWith("https"))
                absolutePaths.add(getAbsolutePathByBitmap(requireContext(), uriToBitmap(requireContext(), Uri.parse(img))))
        }

        if (absolutePaths.isNotEmpty())
            postVm.uploadImgPaths(absolutePaths)
        else
            postVm.updatePost(recordVerUpdate.postId, setRecordUpdateRequest())
    }

    private fun setRecordVerCreate(): RecordRequest {
        recordVerCreate.bookId = bookInLib.id

        val clickedClubs = clubCbRVAdapter.getClickedClubs()
        if (clickedClubs.isEmpty())
            recordVerCreate.scope = "PRIVATE"
        else {
            recordVerCreate.scope = "CLUB"
            recordVerCreate.clubIdListForScope = clickedClubs
        }
        recordVerCreate.location = binding.recordSettingLocationEt.text.toString()
        recordVerCreate.readTime = binding.recordSettingTimeEt.text.toString()
        recordVerCreate.linkRequestDtos = linkRVAdapter.getLinks()

        return recordVerCreate
    }

    private fun setRecordVerUpdate(): PostDetail {
        recordVerUpdate.location = binding.recordSettingLocationEt.text.toString()
        recordVerUpdate.readTime = binding.recordSettingTimeEt.text.toString()
        recordVerUpdate.linkResponseDtos = ArrayList(linkRVAdapter.getLinks())

        val clickedClubs = clubCbRVAdapter.getClickedClubs()
        if (clickedClubs.isEmpty())
            recordVerUpdate.scope = "PRIVATE"
        else {
            recordVerUpdate.scope = "CLUB"
            recordVerUpdate.clubIdListForScope = clickedClubs
        }

        return recordVerUpdate
    }

    private fun setRecordUpdateRequest(): RecordUpdateRequest {
        val recordUpdateReq = RecordUpdateRequest(
            scope = recordVerUpdate.scope,
            isIncomplete = recordVerUpdate.isIncomplete,
            location = binding.recordSettingLocationEt.text.toString(),
            linkRequestDtos = recordVerUpdate.linkResponseDtos,
            readTime = binding.recordSettingTimeEt.text.toString(),
            title = recordVerUpdate.title,
            content = recordVerUpdate.content,
            postImgLocations = recordVerUpdate.postImgLocations,
            clubIdListForScope = clubCbRVAdapter.getClickedClubs()
        )

        val clickedClubs = clubCbRVAdapter.getClickedClubs()
        if (clickedClubs.isEmpty())
            recordUpdateReq.scope = "PRIVATE"
        else {
            recordUpdateReq.scope = "CLUB"
            recordUpdateReq.clubIdListForScope = clickedClubs
        }

        return recordUpdateReq
    }

    private fun mappingToClubFilterEntity(clubs: ArrayList<Club>): ArrayList<ClubFilterEntity> {
        val clubEntities: ArrayList<ClubFilterEntity> = arrayListOf()
        for (club in clubs) {
            if (args.mode=="CREATE")
                clubEntities.add(ClubFilterEntity(club.id, club.name, recordVerCreate.clubIdListForScope.contains(club.id)))
            else
                clubEntities.add(ClubFilterEntity(club.id, club.name, recordVerUpdate.clubIdListForScope.contains(club.id)))
        }

        return clubEntities
    }

    private fun mappingToRecordDetail(record: PostDetail, book: BookInLib): RecordDetail = RecordDetail(
        recordId = record.postId,
        date = record.modifiedDate,
        bookName = book.name,
        writer = null,
        scope = record.scope,
        title = record.title,
        content = record.content,
        photos = record.postImgLocations,
        location = record.location,
        readTime = record.readTime,
        hyperlinks = record.linkResponseDtos,
        likes = record.likedList,
        comments = record.commentsDto,
        clubList = record.clubIdListForScope
    )

    private fun observe() {
        bookVm.createBookCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("RecordSettingFragment", "createBookCode Observe! createBookCode -> $code")

            if (code!=null) {
                dismissLoadingDialog()

                when (code) {
                    201 -> {
                        bookInLib = bookVm.newBookInLib!!

                        if (recordVerCreate.postImgLocations.isEmpty())
                            postVm.createRecord(setRecordVerCreate())
                        else
                            uploadPhotos()
                    }
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })

        postVm.newRecord.observe(viewLifecycleOwner, Observer {
            LogUtil.d("RecordSettingFragment", "newRecord Observe! newRecord -> $it")
            dismissLoadingDialog()

            if (it==null)
                showSnackBar(getString(R.string.error_api))
            else {
                showToast(getString(R.string.msg_create_record_success))
                val action = RecordSettingFragmentDirections.actionRecordSettingFragmentToRecordDetailFragment(mappingToRecordDetail(it, bookInLib))
                findNavController().safeNavigate(action)
            }
        })

        postVm.updateRecord.observe(viewLifecycleOwner, Observer {
            LogUtil.d("RecordSettingFragment", "updateRecord Observe! updateRecord -> $it")
            dismissLoadingDialog()

            if (it==null)
                showSnackBar(getString(R.string.error_api))
            else {
                showToast(getString(R.string.msg_create_record_success))

                if (args.prevPhotos!=null) {
                    val prevPhotos: List<String> = args.prevPhotos!!.toList()
                    val deletePhotos: ArrayList<String> = arrayListOf()

                    for (photos in prevPhotos) {
                        if (!recordVerUpdate.postImgLocations.contains(photos))
                            deletePhotos.add(photos)
                    }

                    if (deletePhotos.isNotEmpty())
                        postVm.deleteMultiplePhotos(deletePhotos)
                }

                val action = RecordSettingFragmentDirections.actionRecordSettingFragmentToRecordDetailFragment(mappingToRecordDetail(it, bookInLib))
                findNavController().safeNavigate(action)
            }
        })

        postVm.uploadImgPaths.observe(viewLifecycleOwner, Observer {
            LogUtil.d("RecordSettingFragment", "uploadImgPaths Observe! uploadImgPaths -> $it")

            if (it==null || it.isEmpty()) {
                dismissLoadingDialog()
                showSnackBar(getString(R.string.error_api))
            } else {
                if (args.mode=="CREATE") {
                    recordVerCreate.postImgLocations = it
                    postVm.createRecord(setRecordVerCreate())
                } else {
                    val postImgLocations: ArrayList<String> = arrayListOf()

                    var itIdx: Int = 0
                    for (i in 0 until recordVerUpdate.postImgLocations.size) {
                        if (recordVerUpdate.postImgLocations[i].startsWith("https"))
                            postImgLocations.add(recordVerUpdate.postImgLocations[i])
                        else {
                            postImgLocations.add(it[itIdx++])
                        }
                    }

                    recordVerUpdate.postImgLocations = postImgLocations
                    postVm.updatePost(recordVerUpdate.postId, setRecordUpdateRequest())
                }
            }
        })

        clubVm.clubList.observe(viewLifecycleOwner, Observer {
            LogUtil.d("RecordSettingFragment", "clubList Observe! clubList -> $it")

            if (it.isNotEmpty()) {
                val clubs: ArrayList<ClubFilterEntity> = mappingToClubFilterEntity(it)
                clubCbRVAdapter.setData(clubs)
            }
        })
    }
}