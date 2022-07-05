package com.mangpo.bookclub.view.main.record

import android.Manifest
import android.net.Uri
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordBinding
import com.mangpo.bookclub.model.domain.Record
import com.mangpo.bookclub.model.entities.RecordRequest
import com.mangpo.bookclub.model.remote.BookInLib
import com.mangpo.bookclub.model.remote.PostDetail
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.RecordPhotoRVAdapter
import gun0912.tedimagepicker.builder.TedImagePicker

class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {
    private val args: RecordFragmentArgs by navArgs()
    private val recordPhotoRVAdapter: RecordPhotoRVAdapter = RecordPhotoRVAdapter()

    private val permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            goGallery()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
        }
    }
    private var photos: ArrayList<String> = arrayListOf()
    private var backPressedFlag: Boolean = false

    private lateinit var myBackPressedCallback: OnBackPressedCallback
    private lateinit var bookInLib: BookInLib
    private lateinit var recordVerCreate: RecordRequest
    private lateinit var recordVerUpdate: PostDetail

    override fun initAfterBinding() {
        myBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressedFlag = true
            }
        }

        setMyEventListener()
        initAdapter()

        if (args.record==null) {

        } else if (args.record!!.postId!=null) {    //메모 수정

        } else {    //아직 등록되지 않은 책에 대한 메모 작성 or 등록된 책에 대한 메모 작성
            binding.recordSelectBookBtn.text = args.record!!.bookTitle
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        myBackPressedCallback.isEnabled = false
        myBackPressedCallback.remove()
    }

    private fun setMyEventListener() {
        binding.recordSelectBookBtn.setOnClickListener {
            findNavController().navigate(R.id.action_recordFragment_to_selectBookFragment)
        }

        binding.recordCameraView.setOnClickListener {
            checkPermission()
        }

        binding.recordNextTv.setOnClickListener {
            hideKeyboard()
            validate()
        }
    }

    private fun initAdapter() {
        recordPhotoRVAdapter.setData(photos)
        recordPhotoRVAdapter.setMyClickListener(object : RecordPhotoRVAdapter.MyClickListener {
            override fun removePhoto(photos: ArrayList<String>) {
                this@RecordFragment.photos = photos
                binding.recordPhotoCntTv.text = photos.size.toString()

                if (photos.size==0) {
                    binding.recordPhotoRv.visibility = View.INVISIBLE
                    binding.recordDescTv.visibility = View.VISIBLE
                }
            }

        })
        binding.recordPhotoRv.adapter = recordPhotoRVAdapter
    }

    private fun checkPermission() {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage(getString(R.string.error_permission_denied))
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check();
    }

    private fun goGallery() {
        TedImagePicker.with(requireContext())
            .title(R.string.title_photos)
            .selectedUri(photos.map { Uri.parse(it) })
            .backButton(R.drawable.ic_back)
            .buttonBackground(R.color.white)
            .buttonTextColor(R.color.primary)
            .savedDirectoryName("Ourpage")
            .max(4, R.string.error_exceed_img)
            .startMultiImage { uriList ->
                binding.recordPhotoCntTv.text = uriList.size.toString()
                photos.clear()

                if (uriList.isEmpty()) {
                    binding.recordPhotoRv.visibility = View.INVISIBLE
                    binding.recordDescTv.visibility = View.VISIBLE
                } else {
                    binding.recordPhotoRv.visibility = View.VISIBLE
                    binding.recordDescTv.visibility = View.INVISIBLE

                    for (uri in uriList)
                        photos.add(uri.toString())
                }

                recordPhotoRVAdapter.setData(photos)
            }
    }

    private fun validate() {
    }
}