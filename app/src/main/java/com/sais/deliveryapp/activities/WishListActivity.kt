package com.sais.deliveryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sais.deliveryapp.databinding.ActivityWishListBinding

class WishListActivity : AppCompatActivity() {

	lateinit var binding:ActivityWishListBinding
	override fun onCreate(savedInstanceState: Bundle?) {

		binding = ActivityWishListBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
}