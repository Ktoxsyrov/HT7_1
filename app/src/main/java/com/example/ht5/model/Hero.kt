package com.example.ht5.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//data class HeroesList(val list: ArrayList<Hero>)

@JsonClass(generateAdapter = true)
data class Hero(
   // val id: Int,
    val localized_name: String,
    val primary_attr: String,
    val attack_type: String,
   // val roles: List<String>,
    val img : String,
    val base_str: Int,
    val base_agi: Int,
    val base_int: Int,
    val str_gain: Float,
    val agi_gain: Float,
    val int_gain: Float
)


