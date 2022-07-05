package com.mangpo.bookclub.model.domain

import android.os.Parcelable
import com.mangpo.bookclub.model.entities.Link
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Record(
    val postId: Int? = null,
    val bookId: Int? = null,
    var bookTitle: String? = null,
    val scope: String? = null,
    val isIncomplete: Boolean = false,
    val location: String? = null,
    val readTime: String? = null,
    val title: String? = null,
    val content: String? = null,
    val photos: @RawValue ArrayList<String> = arrayListOf(),
    val hyperlinks: @RawValue ArrayList<Link> = arrayListOf(),
    val clubIdList: @RawValue ArrayList<Int> = arrayListOf(),
) : Parcelable