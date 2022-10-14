package com.sais.deliveryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sais.deliveryapp.databinding.ActivityCartBinding
import com.sais.deliveryapp.databinding.ActivityOffersBinding

class CartActivity : AppCompatActivity() {
	lateinit var binding: ActivityCartBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityCartBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
}