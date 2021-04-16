package com.dicoding.submissiondua.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.dicoding.submissiondua.BuildConfig
import com.dicoding.submissiondua.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception


class UserViewModel : ViewModel() {

    companion object {
        private const val GITHUB_API = BuildConfig.GITHUB_API_KEY
    }
    private val listUser = MutableLiveData<ArrayList<User>>()

    fun setUser(url: String) {
        val listItems = ArrayList<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $GITHUB_API")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    //parsing json
                    val result = responseBody?.let { String(it) }
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val username = user.getString("login")
                        val avatar = user.getString("avatar_url")
                        val tipe = user.getString("type")
                        val detailUrl = user.getString("url")
                        val followersUrl = user.getString("followers_url")
                        val followingUrl = "https://api.github.com/users/${username}/following"
                        val userItems = User(username, avatar, tipe, detailUrl, followersUrl, followingUrl)
                        listItems.add(userItems)
                    }
                    listUser.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUser
    }
}