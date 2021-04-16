package com.dicoding.submissiondua.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissiondua.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowViewModel : ViewModel() {
    private val listFollowers = MutableLiveData<ArrayList<User>>()

    fun setUser(followUrl: String) {
        val listItems = ArrayList<User>()

        val githubToken = com.dicoding.submissiondua.BuildConfig.GITHUB_API_KEY

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $githubToken")
        client.addHeader("User-Agent", "request")
        client.get(followUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    //parsing json
                    val result = responseBody?.let { String(it) }
                    val list = JSONArray(result)

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val username = user.getString("login")
                        val avatar = user.getString("avatar_url")
                        val tipe = user.getString("type")
                        val detailUrl = user.getString("url")
                        val followersUrl = user.getString("followers_url")
                        val followingUrl = "https://api.github.com/users/${username}/following"
                        val followerItems = User(username, avatar, tipe, detailUrl, followersUrl, followingUrl)
                        listItems.add(followerItems)
                    }
                    listFollowers.postValue(listItems)
                    Log.d("TAG", listFollowers.toString())
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
        return listFollowers
    }
}