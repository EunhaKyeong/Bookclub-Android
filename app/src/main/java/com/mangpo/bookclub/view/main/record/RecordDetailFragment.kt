package com.mangpo.bookclub.view.main.record

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordDetailBinding
import com.mangpo.bookclub.model.domain.Record
import com.mangpo.bookclub.model.domain.RecordDetail
import com.mangpo.bookclub.utils.*
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.LinkVerDetailRVAdapter
import com.mangpo.bookclub.view.adpater.RecordPhotoVPAdapter
import com.mangpo.bookclub.view.dialog.ActionDialogFragment
import com.mangpo.bookclub.viewmodel.PostViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordDetailFragment : BaseFragment<FragmentRecordDetailBinding>(FragmentRecordDetailBinding::inflate) {
    private val postVm: PostViewModel by viewModels<PostViewModel>()
    private val args: RecordDetailFragmentArgs by navArgs()

    private lateinit var recordPhotoVPAdapter: RecordPhotoVPAdapter
    private lateinit var actionDialogFragment: ActionDialogFragment
    private lateinit var recordDetail: RecordDetail

    override fun initAfterBinding() {
        recordDetail = args.record

        setMyEventListener()
        initAdapter()
        initActionDialogFragment()
        bind()
        observe()
    }

    private fun initAdapter() {
        recordPhotoVPAdapter = RecordPhotoVPAdapter()
        recordPhotoVPAdapter.setMyCLickListener(object : RecordPhotoVPAdapter.MyClickListener {
            override fun goPhotoActivity(photos: List<String>, position: Int) {
                val action = RecordDetailFragmentDirections.actionRecordDetailFragmentToPhotoViewActivity(photos.toTypedArray(), position)
                findNavController().navigate(action)
            }

        })
        binding.recordDetailPhotoVp.adapter = recordPhotoVPAdapter
    }

    private fun initActionDialogFragment() {
        actionDialogFragment = ActionDialogFragment()

        val bundle: Bundle = Bundle()
        bundle.apply {
            putString("title", getString(R.string.msg_delete_memo))
            putString("desc", getString(R.string.msg_delete_memo_desc))
        }
        actionDialogFragment.arguments = bundle

        actionDialogFragment.setMyDialogCallback(object : ActionDialogFragment.MyDialogCallback {
            override fun action1() {
                showLoadingDialog()

                if (!isNetworkAvailable(requireContext())) {
                    dismissLoadingDialog()
                    showNetworkSnackBar()
                } else
                    postVm.deletePost(recordDetail.recordId)
            }

            override fun action2() {
            }
        })
    }

    private fun setMyEventListener() {
        binding.recordDetailPhotoVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.recordDetailIndicator.setSelected(position)
            }
        })

        binding.recordDetailUpdateBtn.setOnClickListener {
            val action = RecordDetailFragmentDirections.actionRecordDetailFragmentToRecordFragment(mappingToRecord(recordDetail))
            findNavController().navigate(action)
        }

        binding.recordDetailDeleteBtn.setOnClickListener {
            actionDialogFragment.show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun mappingToRecord(recordDetail: RecordDetail): Record = Record(
        postId = recordDetail.recordId,
        bookTitle = recordDetail.bookName,
        scope = recordDetail.scope,
        location = recordDetail.location,
        readTime = recordDetail.readTime,
        title = recordDetail.title,
        content = recordDetail.content,
        photos = recordDetail.photos,
        hyperlinks = recordDetail.hyperlinks,
        clubIdList = recordDetail.clubList
    )

    private fun bind() {
        binding.recordDetailBookNameTv.text = recordDetail.bookName

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        val date: LocalDateTime = LocalDateTime.parse(recordDetail.date.substring(0, 19), formatter1)
        binding.recordDetailRecordDateTv.text = date.format(formatter2)

        if (recordDetail.writer==null) {
            binding.recordDetailUpdateBtn.visibility = View.VISIBLE
            binding.recordDetailDeleteBtn.visibility = View.VISIBLE
            binding.recordDetailNicknameTv.visibility = View.INVISIBLE
            binding.recordDetailNicknameTextTv.visibility = View.INVISIBLE


        } else {
            binding.recordDetailUpdateBtn.visibility = View.INVISIBLE
            binding.recordDetailDeleteBtn.visibility = View.INVISIBLE
            binding.recordDetailNicknameTv.text = recordDetail.writer
            binding.recordDetailNicknameTv.visibility = View.VISIBLE
            binding.recordDetailNicknameTextTv.visibility = View.VISIBLE
        }

        binding.recordDetailRecordTitleTv.text = recordDetail.title

        binding.recordDetailRecordContentTv.text = recordDetail.content

        if (recordDetail.photos.isEmpty()) {
            binding.recordDetailPhotoVp.visibility = View.GONE
            binding.recordDetailIndicator.visibility = View.GONE
        } else {
            val size = getDeviceWidth() - convertDpToPx(requireContext(), 40)
            val params = binding.recordDetailPhotoVp.layoutParams
            params.width = size
            params.height = size
            binding.recordDetailPhotoVp.layoutParams = params

            recordPhotoVPAdapter.setData(recordDetail.photos)
            binding.recordDetailIndicator.count = recordPhotoVPAdapter.itemCount
            binding.recordDetailPhotoVp.visibility = View.VISIBLE
            binding.recordDetailIndicator.visibility = View.VISIBLE
        }

        if (recordDetail.location.isBlank()) {
            binding.recordDetailLocationTv.text = getString(R.string.msg_input_location)
            binding.recordDetailLocationTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))
        } else {
            binding.recordDetailLocationTv.text = recordDetail.location
            binding.recordDetailLocationTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
        }

        if (recordDetail.readTime.isBlank()) {
            binding.recordDetailTimeTv.text = getString(R.string.msg_input_time)
            binding.recordDetailTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))
        } else {
            binding.recordDetailTimeTv.text = recordDetail.readTime
            binding.recordDetailTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
        }

        if (recordDetail.hyperlinks.isEmpty()) {
            binding.recordDetailLinkHintTv.text = getString(R.string.msg_input_link)
            binding.recordDetailLinkHintTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))

            binding.recordDetailLinkHintTv.visibility = View.VISIBLE
            binding.recordDetailLinkHintIv.visibility = View.VISIBLE
            binding.recordDetailLinkRv.visibility = View.GONE
        } else {
            val linkVerDetailRVAdapter: LinkVerDetailRVAdapter = LinkVerDetailRVAdapter()
            linkVerDetailRVAdapter.setData(recordDetail.hyperlinks)
            binding.recordDetailLinkRv.adapter = linkVerDetailRVAdapter

            binding.recordDetailLinkHintTv.visibility = View.GONE
            binding.recordDetailLinkHintIv.visibility = View.GONE
            binding.recordDetailLinkRv.visibility = View.VISIBLE
        }
    }

    private fun observe() {
        postVm.deletePostCode.observe(viewLifecycleOwner, Observer {
            LogUtil.d("RecordDetailFragment", "deletePostCode Observe! deletePostCode -> $it")

            if (it==204) {
                if (recordDetail.photos.isEmpty()) {
                    dismissLoadingDialog()
                    findNavController().popBackStack()
                } else
                    postVm.deleteMultiplePhotos(recordDetail.photos)
            } else {
                dismissLoadingDialog()
                showSnackBar(getString(R.string.error_api))
            }
        })

        postVm.deletePhotosCode.observe(viewLifecycleOwner, Observer {
            LogUtil.d("RecordDetailFragment", "deletePhotosCode Observe! deletePhotosCode -> $it")
            showToast(getString(R.string.msg_delete_record))

            if (recordDetail.photos.isNotEmpty())
                postVm.deleteMultiplePhotos(recordDetail.photos)

            dismissLoadingDialog()
            findNavController().popBackStack()
        })
    }
}
