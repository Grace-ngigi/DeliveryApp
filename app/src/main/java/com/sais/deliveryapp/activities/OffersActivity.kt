package com.sais.deliveryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sais.deliveryapp.databinding.ActivityOffersBinding

class OffersActivity : AppCompatActivity() {
	lateinit var binding: ActivityOffersBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityOffersBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
}