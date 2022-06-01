package com.example.ht5.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import coil.load
import com.bumptech.glide.Glide
import com.example.ht5.MainActivity
import com.example.ht5.R
import com.example.ht5.databinding.HeroItemBinding
import com.example.ht5.model.Hero

class HeroesAdapter(): RecyclerView.Adapter<HeroesAdapter.HeroViewHolder>() {

    private var heroList: MutableList<Hero>? = null

    fun setHeroList(heroList: MutableList<Hero>?){
        this.heroList = heroList
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(heroList?.get(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HeroItemBinding.inflate(layoutInflater)
        return HeroViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return heroList?.size!!
    }

    fun getHeroByPos(pos: Int): Hero {
        return heroList!![pos]
    }
    class HeroViewHolder(private val binding: HeroItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(hero: Hero){
            binding.heroData = hero
            binding.heroAvatar.load("https://api.opendota.com" + hero.img)
            when(hero.primary_attr){
                "str" -> binding.primaryAttr.setImageResource(R.drawable.str)
                "int" -> binding.primaryAttr.setImageResource(R.drawable.intell)
                "agi" -> binding.primaryAttr.setImageResource(R.drawable.agi)
            }
        }
    }


    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(avatar: ImageView, url: String) {
          avatar.load("https://api.opendota.com/$url")
        }
    }

}