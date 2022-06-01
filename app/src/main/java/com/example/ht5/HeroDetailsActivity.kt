package com.example.ht5

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import coil.load
import com.example.ht5.databinding.HeroDetailsBinding
import com.example.ht5.model.Hero

private lateinit var binding: HeroDetailsBinding

class HeroDetailsActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = HeroDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.heroDetailsAvatar.load("https://api.opendota.com" + intent.getStringExtra("img"))
        binding.heroDetailsName.text = intent.getStringExtra("name")
        binding.strStats.text = "${intent.getStringExtra("str")} + ${intent.getStringExtra("strGain")}"
        binding.intStats.text = "${intent.getStringExtra("int")} + ${intent.getStringExtra("intGain")}"
        binding.agiStats.text = "${intent.getStringExtra("agi")} + ${intent.getStringExtra("agiGain")}"
        if(intent.getStringExtra("attackType") == "Melee")
            binding.heroDetailsAttackType.setImageResource(R.drawable.ic_melee)
        else
            binding.heroDetailsAttackType.setImageResource(R.drawable.ic_ranged)
        when(intent.getStringExtra("attr")){
            "int" -> {
                binding.intStats.typeface = Typeface.DEFAULT_BOLD
                binding.heroDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.main_int))
            }
            "agi" -> {
                binding.agiStats.typeface = Typeface.DEFAULT_BOLD
                binding.heroDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.main_agi))
            }
            "str" -> {
                binding.strStats.typeface = Typeface.DEFAULT_BOLD
                binding.heroDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.main_str))
            }
        }
    }
}