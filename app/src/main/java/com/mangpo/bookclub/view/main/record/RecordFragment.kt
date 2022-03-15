package com.mangpo.bookclub.view.main.record

import android.Manifest
import android.net.Uri
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordBinding
import com.mangpo.bookclub.model.entities.RecordRequest
import com.mangpo.bookclub.model.remote.Book
import com.mangpo.bookclub.model.remote.RecordResponse
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.PrefsUtils
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
    private lateinit var book: Book
    private lateinit var recordVerCreate: RecordRequest
    private lateinit var recordVerUpdate: RecordResponse

    override fun initAfterBinding() {
        myBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressedFlag = true
                PrefsUtils.setTempRecord("")
            }
        }

        setMyEventListener()
        initAdapter()

        if (args.mode=="CREATE" && PrefsUtils.getTempRecord().isNotBlank()) {
            LogUtil.d("RecordFragment", "args.mode==\"CREATE\" && PrefsUtils.getTempRecord().isNotBlank()")
            recordVerCreate = Gson().fromJson(PrefsUtils.getTempRecord(), RecordRequest::class.java)
            bindRecordVerCreate(recordVerCreate)
        } else if (args.mode=="UPDATE" && PrefsUtils.getTempRecord().isBlank()) {
            LogUtil.d("RecordFragment", "args.mode==\"UPDATE\"")
            recordVerUpdate = Gson().fromJson(args.record, RecordResponse::class.java)
            bindRecordVerUpdate(recordVerUpdate)
        } else if (args.mode=="UPDATE" && PrefsUtils.getTempRecord().isNotBlank()) {
            LogUtil.d("RecordFragment", "args.mode==\"UPDATE\" && PrefsUtils.getTempRecord().isNotBlank()")
            recordVerUpdate = Gson().fromJson(PrefsUtils.getTempRecord(), RecordResponse::class.java)
            bindRecordVerUpdate(recordVerUpdate)
        }

        if (args.book!=null) {
            book = Gson().fromJson(args.book, Book::class.java)
            bindBook()
        }
    }

    override fun onStop() {
        super.onStop()
        LogUtil.d("RecordFragment", "onStop")

        if (!backPressedFlag && args.mode=="CREATE")
            PrefsUtils.setTempRecord(Gson().toJson(setRecordVerCreate()))
        else if (!backPressedFlag && args.mode=="UPDATE")
            PrefsUtils.setTempRecord(Gson().toJson(setRecordVerUpdate()))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        myBackPressedCallback.isEnabled = false
        myBackPressedCallback.remove()
    }

    private fun setMyEventListener() {
        binding.recordSelectBookBtn.setOnClickListener {
            PrefsUtils.setTempRecord(Gson().toJson(setRecordVerCreate()))
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

    private fun bindRecordVerCreate(record: RecordRequest) {
        binding.recordSelectBookBtn.isEnabled = true

        binding.recordTitleEt.setText(record.title)
        binding.recordContentEt.setText(record.content)

        photos = record.postImgLocations as ArrayList<String>
        binding.recordPhotoCntTv.text = photos.size.toString()
        if (record.postImgLocations.isNotEmpty()) {
            binding.recordPhotoRv.visibility = View.VISIBLE
            recordPhotoRVAdapter.setData(record.postImgLocations as ArrayList<String>)
            binding.recordDescTv.visibility = View.INVISIBLE
        }
    }

    private fun bindRecordVerUpdate(record: RecordResponse) {
        binding.recordSelectBookBtn.isEnabled = false

        binding.recordTitleEt.setText(record.title)
        binding.recordContentEt.setText(record.content)

        photos = record.postImgLocations as ArrayList<String>
        binding.recordPhotoCntTv.text = photos.size.toString()
        if (record.postImgLocations.isNotEmpty()) {
            binding.recordPhotoRv.visibility = View.VISIBLE
            recordPhotoRVAdapter.setData(record.postImgLocations as ArrayList<String>)
            binding.recordDescTv.visibility = View.INVISIBLE
        }
    }

    private fun bindBook() {
        binding.recordSelectBookBtn.text = book.name
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

    private fun setRecordVerCreate(): RecordRequest {
        if (!::recordVerCreate.isInitialized)
            recordVerCreate = RecordRequest()

        recordVerCreate.title = binding.recordTitleEt.text.toString()
        recordVerCreate.content = binding.recordContentEt.text.toString()
        recordVerCreate.postImgLocations = photos

        return recordVerCreate
    }

    private fun setRecordVerUpdate(): RecordResponse {
        recordVerUpdate.title = binding.recordTitleEt.text.toString()
        recordVerUpdate.content = binding.recordContentEt.text.toString()
        recordVerUpdate.postImgLocations = photos

        return recordVerUpdate
    }

    private fun validate() {
        if (binding.recordSelectBookBtn.text==getString(R.string.msg_select_book))
            showToast(getString(R.string.error_select_book))
        else if (binding.recordTitleEt.text.isBlank() || binding.recordContentEt.text.isBlank())
            showToast(getString(R.string.error_input_title_content))
        else if (args.mode=="CREATE") {
            PrefsUtils.setTempRecord("")
            val action = RecordFragmentDirections.actionRecordFragmentToRecordSettingFragment("CREATE", Gson().toJson(setRecordVerCreate()), Gson().toJson(book), null)
            findNavController().navigate(action)
        } else {
            PrefsUtils.setTempRecord("")
            val prevPhotos = recordVerUpdate.postImgLocations

            if (prevPhotos.isEmpty()) {
                val action = RecordFragmentDirections.actionRecordFragmentToRecordSettingFragment("UPDATE", Gson().toJson(setRecordVerUpdate()), Gson().toJson(book), null)
                findNavController().navigate(action)
            } else {
                val action = RecordFragmentDirections.actionRecordFragmentToRecordSettingFragment("UPDATE", Gson().toJson(setRecordVerUpdate()), Gson().toJson(book), prevPhotos.toTypedArray())
                findNavController().navigate(action)
            }
        }
    }
}