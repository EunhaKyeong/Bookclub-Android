package com.mangpo.bookclub.model.entities

data class RecordRequest(
    var bookId: Int? = null,
    var scope: String? = null,
    var isIncomplete: Boolean = false,
    var location: String? = null,
    var readTime: String? = null,
    var linkRequestDtos: List<Link> = listOf(),
    var title: String? = null,
    var content: String? = null,
    var postImgLocations: List<String> = listOf(),
    var clubIdListForScope: ArrayList<Int> = arrayListOf()
)

data class RecordUpdateRequest(
    var scope: String? = null,
    var isIncomplete: Boolean = false,
    var location: String? = null,
    var readTime: String? = null,
    var linkRequestDtos: List<Link> = listOf(),
    var title: String? = null,
    var content: String? = null,
    var postImgLocations: List<String> = listOf(),
    var clubIdListForScope: List<Int> = listOf()
)

data class Link (
    var linkId: Int? = null,
    var hyperlinkTitle: String,
    var hyperlink: String
)
