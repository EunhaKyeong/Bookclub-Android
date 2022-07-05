package com.mangpo.bookclub.model.domain

import android.os.Parcelable
import com.mangpo.bookclub.model.entities.Link
import com.mangpo.bookclub.model.remote.Comment
import com.mangpo.bookclub.model.remote.Liked
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class RecordDetail(
    var recordId: Int,
    var date: String,
    var bookName: String,
    var writer: String?,
    var scope: String,
    var title: String,
    var content: String,
    var photos: ArrayList<String>,
    var location: String,
    var readTime: String,
    var hyperlinks: @RawValue ArrayList<Link>,
    var likes: @RawValue ArrayList<Liked>,
    var comments: @RawValue ArrayList<Comment>,
    var clubList: @RawValue ArrayList<Int>
) : Parcelable