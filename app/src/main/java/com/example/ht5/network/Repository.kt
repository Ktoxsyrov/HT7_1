package com.example.ht5.network

import com.example.ht5.model.Hero
import com.squareup.moshi.*
import okhttp3.*
import java.io.IOException

class Repository {
    private val URL = "https://api.opendota.com/api/heroStats"
    private val request = Request.Builder().url(URL).build()
    private val okHttpClient = OkHttpClient()

    fun makeApiCall(heroesList: MutableList<Hero>?) {

        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback{
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                val moshi = Moshi.Builder().build()
                val listHero = Types.newParameterizedType(List::class.java, Hero::class.java)
                val jsonAdapter: JsonAdapter<List<Hero>> = moshi.adapter(listHero)
                val heroes = jsonAdapter.fromJson(body!!)
                println(heroes?.get(15))
                for(i in 0 until heroes!!.size) {
                    heroesList?.add(heroes[i])
                    println(heroesList?.get(i))
                }

            }
        })
    }



}


