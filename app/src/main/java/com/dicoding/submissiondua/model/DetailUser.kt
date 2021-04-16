package com.dicoding.submissiondua.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailUser(
    var username: String?,
    var bio: String?,
    var location: String?,
    var company: String?,
    var avatar: String?,
    var repository: Int?,
    var followers: Int?,
    var following: Int?
) : Parcelable