package com.dicoding.submissiondua.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissiondua.model.DetailUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailUserViewModel : ViewModel() {

    companion object {
        private const val GITHUB_API = com.dicoding.submissiondua.BuildConfig.GITHUB_API_KEY
    }

    private val listDetailUser = MutableLiveData<DetailUser>()

    fun setDetailUser(url: String) {

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $GITHUB_API")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    val responseObject = JSONObject(result)

                    val username = responseObject.getString("login")
                    val biografi = responseObject.getString("bio")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("company")
                    val avatar = responseObject.getString("avatar_url")
                    val repository = responseObject.getInt("public_repos")
                    val followers = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")

                    val userDetail = DetailUser(
                        username,
                        biografi,
                        location,
                        company,
                        avatar,
                        repository,
                        followers,
                        following
                    )
                    listDetailUser.postValue(userDetail)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?,
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getDetailUser(): LiveData<DetailUser> {
        return listDetailUser
    }
}