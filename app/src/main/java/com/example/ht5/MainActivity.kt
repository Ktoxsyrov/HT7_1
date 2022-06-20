package com.example.ht5

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ht5.adapter.HeroesAdapter
import com.example.ht5.model.Hero
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*
import java.lang.Thread.sleep

private lateinit var heroRecyclerAdapter: HeroesAdapter

interface OnItemClickListener{
    fun onItemClicked(position: Int, view: View)
}

class MainActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val heroes = mutableListOf<Hero>()



        makeApiCall(heroes)
        println(heroes.size)

        while(heroes.size==0)
            sleep(100)

        val recyclerView = findViewById<RecyclerView>(R.id.heroesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        heroRecyclerAdapter = HeroesAdapter()
        heroRecyclerAdapter.setHeroList(heroes)
        recyclerView.adapter = heroRecyclerAdapter

        recyclerView.addOnItemCLickListener(object: OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                val clickedHero = heroRecyclerAdapter.getHeroByPos(position)

                val intent = Intent(this@MainActivity, HeroDetailsActivity::class.java)
                intent.putExtra("name", clickedHero.localized_name)
                intent.putExtra("img", clickedHero.img)
                intent.putExtra("str", clickedHero.base_str.toString())
                intent.putExtra("strGain", clickedHero.str_gain.toString())
                intent.putExtra("int", clickedHero.base_int.toString())
                intent.putExtra("intGain", clickedHero.int_gain.toString())
                intent.putExtra("agi", clickedHero.base_agi.toString())
                intent.putExtra("agiGain", clickedHero.agi_gain.toString())
                intent.putExtra("attackType", clickedHero.attack_type)
                intent.putExtra("attr", clickedHero.primary_attr)

                startActivity(intent)
            }
        })
    }

    private fun RecyclerView.addOnItemCLickListener(onClickListener: OnItemClickListener){
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

    private val URL = "https://api.opendota.com/api/heroStats"
    private val request = Request.Builder().url(URL).build()
    private val okHttpClient = OkHttpClient()

    private fun makeApiCall(heroesList: MutableList<Hero>?) {
        val dataFromFile = readFile()
        if (dataFromFile != null) {
            println("From file")
            val moshi = Moshi.Builder().build()
            val listHero = Types.newParameterizedType(List::class.java, Hero::class.java)
            val jsonAdapter: JsonAdapter<List<Hero>> = moshi.adapter(listHero)
            val heroes = jsonAdapter.fromJson(dataFromFile!!)
            println(heroes?.get(15))
            for (i in 0 until heroes!!.size) {
                heroesList?.add(heroes[i])
               // println(heroesList?.get(i))
            }
        } else {
            println("From network")
            okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    //ничего
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    writeFile(body)
                    println(body)
                    val moshi = Moshi.Builder().build()
                    val listHero = Types.newParameterizedType(List::class.java, Hero::class.java)
                    val jsonAdapter: JsonAdapter<List<Hero>> = moshi.adapter(listHero)
                    val heroes = jsonAdapter.fromJson(body!!)
                    println(heroes?.get(15))
                    for (i in 0 until heroes!!.size) {
                        heroesList?.add(heroes[i])
                       // println(heroesList?.get(i))
                    }

                }
            })
        }
    }

    private fun writeFile(json: String?){
        try{
            val bw = BufferedWriter(OutputStreamWriter(openFileOutput("dotaHeroesFile", MODE_PRIVATE)))
            bw.write(json)
            bw.close()
        } catch (e: FileNotFoundException){
            println("Файл не найден")
        }
    }
    private fun readFile():String?{
        try {
            val br = BufferedReader(InputStreamReader(openFileInput("dotaHeroesFile")))
            return br.readText()
        }catch (e: FileNotFoundException){
            return null
        }
    }
}