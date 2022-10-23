package com.sais.deliveryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sais.deliveryapp.databinding.ActivityCartItemDescBinding

class CartItemDescActivity : AppCompatActivity() {

	lateinit var binding: ActivityCartItemDescBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityCartItemDescBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
}